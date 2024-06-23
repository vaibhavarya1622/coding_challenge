package org.vaibhav.MemoryStore;

import java.util.HashMap;
import java.util.Map;

public class RedisStore {
    private static final RedisStore UNIQUE_INSTANCE = new RedisStore();
    private final Map<String, String> redisStore = new HashMap<>();
    private final Map<String,Long> ttlStore = new HashMap<>();

    private RedisStore() {

    }
    public static RedisStore getInstance() {
        return UNIQUE_INSTANCE;
    }
    public void SET(String key, String value) {
        redisStore.put(key, value);
    }
    public String GET(String key) {
        return redisStore.get(key);
    }

    public void SETTTL(String key, Long ttl) {
        ttlStore.put(key, ttl);
    }

    public Long GETTTL(String key) {
        return ttlStore.get(key);
    }
    public void DEL(String key) {
        redisStore.remove(key);
        ttlStore.remove(key);
    }
    public Boolean EXIST(String key) {
        return redisStore.containsKey(key);
    }
}
