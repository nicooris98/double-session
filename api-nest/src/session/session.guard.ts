import {
  CanActivate,
  createParamDecorator,
  ExecutionContext,
  Injectable,
  UnauthorizedException,
} from '@nestjs/common';
import { Request } from 'express';
import { SESSION_COOKIE, SessionService, SharedSession } from './session.service';

type RequestConSesion = Request & { sesion?: SharedSession };

/**
 * Equivalente al SharedSessionFilter del legacy: lee la cookie SHARED_SESSION,
 * busca la sesión en Redis y responde 401 si no existe.
 */
@Injectable()
export class SessionGuard implements CanActivate {
  constructor(private readonly sessions: SessionService) {}

  async canActivate(context: ExecutionContext): Promise<boolean> {
    const request = context.switchToHttp().getRequest<RequestConSesion>();
    const id = request.cookies?.[SESSION_COOKIE];
    const sesion = id ? await this.sessions.obtenerYRenovar(id) : null;
    if (!sesion) {
      throw new UnauthorizedException('Sesión inexistente o expirada');
    }
    request.sesion = sesion;
    return true;
  }
}

/** Inyecta en el controller la sesión validada por SessionGuard. */
export const Sesion = createParamDecorator(
  (_: unknown, context: ExecutionContext) => context.switchToHttp().getRequest<RequestConSesion>().sesion,
);
