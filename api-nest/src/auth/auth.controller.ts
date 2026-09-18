import { Controller, Get, HttpCode, Post, Req, Res, UseGuards } from '@nestjs/common';
import { Request, Response } from 'express';
import { Sesion, SessionGuard } from '../session/session.guard';
import { SESSION_COOKIE, SessionService, SharedSession } from '../session/session.service';

/**
 * La migración NO tiene login propio: la sesión la crea el legacy.
 * Aquí solo se consulta y se cierra.
 */
@Controller('auth')
export class AuthController {
  constructor(private readonly sessions: SessionService) {}

  @Get('me')
  @UseGuards(SessionGuard)
  async me(@Sesion() sesion: SharedSession) {
    return { usuario: sesion, ttl: await this.sessions.ttl(sesion.id) };
  }

  /** Cierra la sesión en ambos sistemas (el legacy también la valida contra Redis). */
  @Post('logout')
  @HttpCode(200)
  async logout(@Req() req: Request, @Res({ passthrough: true }) res: Response) {
    const id = req.cookies?.[SESSION_COOKIE];
    if (id) {
      await this.sessions.eliminar(id);
    }
    res.clearCookie(SESSION_COOKIE, { path: '/' });
    return { ok: true };
  }
}
