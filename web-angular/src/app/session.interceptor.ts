import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { LEGACY_LOGIN_URL } from './auth.service';

/** Si la API responde 401 (sesión expirada o cerrada en el legacy), se vuelve al login del legacy. */
export const sessionInterceptor: HttpInterceptorFn = (req, next) =>
  next(req.clone({ withCredentials: true })).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401) {
        window.location.href = LEGACY_LOGIN_URL + '?expirada=1';
      }
      return throwError(() => err);
    }),
  );
