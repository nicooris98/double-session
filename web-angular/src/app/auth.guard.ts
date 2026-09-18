import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from './auth.service';

/** Antes de mostrar cualquier pantalla, confirma que existe la sesión compartida. */
export const authGuard: CanActivateFn = () =>
  inject(AuthService)
    .cargarSesion()
    .pipe(
      map(() => true),
      // El 401 ya lo maneja el interceptor (redirige al login legacy)
      catchError(() => of(false)),
    );
