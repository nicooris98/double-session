import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit, signal } from '@angular/core';
import { AuthService, LEGACY_LOGIN_URL, LEGACY_MENU_URL } from './auth.service';

interface Cliente {
  id: number;
  nombre: string;
  rut: string;
  ejecutivo: string;
}

/** Módulo migrado (Angular + NestJS) al que se llega desde el menú del legacy. */
@Component({
  selector: 'app-clientes',
  template: `
    <div class="header">
      <span>Sistema Corporativo <span class="tag">NUEVO · Angular + NestJS</span></span>
      <span>{{ auth.sesion()?.usuario?.nombre }}</span>
    </div>

    <div class="card">
      <h2>Clientes (módulo migrado)</h2>
      <p>
        Hola <b>{{ auth.sesion()?.usuario?.nombre }}</b>, llegaste desde el menú del legacy sin volver a
        iniciar sesión.
      </p>

      <table>
        <tr><th>ID</th><th>Nombre</th><th>RUT</th><th>Ejecutivo</th></tr>
        @for (c of clientes(); track c.id) {
          <tr><td>{{ c.id }}</td><td>{{ c.nombre }}</td><td>{{ c.rut }}</td><td>{{ c.ejecutivo }}</td></tr>
        }
      </table>

      <div class="acciones">
        <a class="btn btn-primary" [href]="menuUrl">Volver al menú</a>
        <button class="btn btn-light" (click)="logout()">Cerrar sesión</button>
      </div>

      <p class="muted">
        Sesión compartida: <b>shared-session:{{ auth.sesion()?.usuario?.id }}</b><br />
        Roles: {{ auth.sesion()?.usuario?.roles?.join(', ') }} · TTL en Redis: {{ auth.sesion()?.ttl }} s
      </p>
    </div>
  `,
})
export class ClientesComponent implements OnInit {
  private readonly http = inject(HttpClient);
  readonly auth = inject(AuthService);
  readonly clientes = signal<Cliente[]>([]);
  readonly menuUrl = LEGACY_MENU_URL;

  ngOnInit() {
    this.http.get<Cliente[]>('/api/clientes').subscribe((c) => this.clientes.set(c));
  }

  logout() {
    this.auth.logout().subscribe(() => (window.location.href = LEGACY_LOGIN_URL + '?logout=1'));
  }
}
