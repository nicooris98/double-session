import { Module } from '@nestjs/common';
import { AuthController } from './auth/auth.controller';
import { ClientesController } from './clientes/clientes.controller';
import { SessionService } from './session/session.service';

@Module({
  controllers: [AuthController, ClientesController],
  providers: [SessionService],
})
export class AppModule {}
