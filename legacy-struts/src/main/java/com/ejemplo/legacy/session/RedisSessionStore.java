package com.ejemplo.legacy.session;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.UUID;

/**
 * Sesión compartida guardada en Redis como JSON.
 *
 * Se usa JSON (y no la HttpSession serializada de Java) para que NestJS
 * pueda leer exactamente la misma sesión.
 *
 * Clave:  shared-session:{uuid}
 * Valor:  {"id","userId","username","nombre","roles","loginAt"}
 * TTL:    TTL_SECONDS, renovado en cada request (expiración deslizante)
 */
public final class RedisSessionStore {

    public static final String KEY_PREFIX = "shared-session:";
    public static final int TTL_SECONDS = 30 * 60;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<Map<String, Object>>() {};
    private static final JedisPool POOL = new JedisPool(
            env("REDIS_HOST", "localhost"),
            Integer.parseInt(env("REDIS_PORT", "6379")));

    private RedisSessionStore() {
    }

    /** Crea la sesión en Redis y devuelve su id (el valor de la cookie). */
    public static String crear(Map<String, Object> datos) {
        String id = UUID.randomUUID().toString();
        datos.put("id", id);
        datos.put("loginAt", System.currentTimeMillis());
        try (Jedis jedis = POOL.getResource()) {
            jedis.setex(KEY_PREFIX + id, TTL_SECONDS, MAPPER.writeValueAsString(datos));
            return id;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo crear la sesión en Redis", e);
        }
    }

    /** Devuelve la sesión y renueva su TTL, o null si no existe / expiró. */
    public static Map<String, Object> obtenerYRenovar(String id) {
        String key = KEY_PREFIX + id;
        try (Jedis jedis = POOL.getResource()) {
            String json = jedis.get(key);
            if (json == null) {
                return null;
            }
            jedis.expire(key, TTL_SECONDS);
            return MAPPER.readValue(json, MAP_TYPE);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo leer la sesión en Redis", e);
        }
    }

    public static long ttl(String id) {
        try (Jedis jedis = POOL.getResource()) {
            return jedis.ttl(KEY_PREFIX + id);
        }
    }

    public static void eliminar(String id) {
        try (Jedis jedis = POOL.getResource()) {
            jedis.del(KEY_PREFIX + id);
        }
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isEmpty() ? defaultValue : value;
    }
}
