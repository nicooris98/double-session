import { Injectable, OnModuleDestroy } from '@nestjs/common';
import Redis from 'ioredis';

/** Mismo contrato que escribe el legacy en RedisSessionStore.java */
export interface SharedSession {
  id: string;
  userId: number;
  username: string;
  nombre: string;
  roles: string[];
  loginAt: number;
}

export const SESSION_COOKIE = 'SHARED_SESSION';
const KEY_PREFIX = 'shared-session:';
const TTL_SECONDS = 30 * 60;

@Injectable()
export class SessionService implements OnModuleDestroy {
  private readonly redis = new Redis({
    host: process.env.REDIS_HOST ?? 'localhost',
    port: Number(process.env.REDIS_PORT ?? 6379),
  });

  /** Devuelve la sesión y renueva su TTL (expiración deslizante), o null si no existe. */
  async obtenerYRenovar(id: string): Promise<SharedSession | null> {
    const key = KEY_PREFIX + id;
    const json = await this.redis.get(key);
    if (!json) {
      return null;
    }
    await this.redis.expire(key, TTL_SECONDS);
    return JSON.parse(json);
  }

  ttl(id: string): Promise<number> {
    return this.redis.ttl(KEY_PREFIX + id);
  }

  async eliminar(id: string): Promise<void> {
    await this.redis.del(KEY_PREFIX + id);
  }

  onModuleDestroy() {
    this.redis.disconnect();
  }
}
