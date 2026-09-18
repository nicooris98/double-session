import { Routes } from '@angular/router';
import { authGuard } from './auth.guard';
import { ClientesComponent } from './clientes.component';

export const routes: Routes = [
  { path: '', component: ClientesComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' },
];
