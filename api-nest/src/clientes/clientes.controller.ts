import { Controller, Get, UseGuards } from '@nestjs/common';
import { Sesion, SessionGuard } from '../session/session.guard';
import { SharedSession } from '../session/session.service';

/** Módulo ya migrado (ej.: clientes). Protegido por la misma sesión del legacy. */
@Controller('clientes')
@UseGuards(SessionGuard)
export class ClientesController {
  @Get()
  listar(@Sesion() sesion: SharedSession) {
    const clientes = [
      { id: 1, nombre: 'Comercial Andes', rut: '76.111.111-1', ejecutivo: 'admin' },
      { id: 2, nombre: 'Distribuidora Sur', rut: '76.222.222-2', ejecutivo: 'user' },
      { id: 3, nombre: 'Tecnología Norte', rut: '76.333.333-3', ejecutivo: 'admin' },
    ];
    // Ejemplo de autorización con los roles que puso el legacy en la sesión
    return sesion.roles.includes('ADMIN') ? clientes : clientes.filter((c) => c.ejecutivo === sesion.username);
  }
}
