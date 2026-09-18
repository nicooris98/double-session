import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { tap } from 'rxjs';

export interface Usuario {
  id: string;
  userId: number;
  username: string;
  nombre: string;
  roles: string[];
  loginAt: number;
}

export interface InfoSesion {
  usuario: Usuario;
  ttl: number;
}

/** URL del login del legacy: la migración no tiene login propio. */
export const LEGACY_LOGIN_URL = '/legacy/login.do';
export const LEGACY_MENU_URL = '/legacy/menu.do';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  readonly sesion = signal<InfoSesion | null>(null);

  /** La cookie HttpOnly SHARED_SESSION viaja sola; Nest la valida contra Redis. */
  cargarSesion() {
    return this.http.get<InfoSesion>('/api/auth/me').pipe(tap((s) => this.sesion.set(s)));
  }

  logout() {
    return this.http.post('/api/auth/logout', {});
  }
}
