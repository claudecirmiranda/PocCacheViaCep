package com.example.demo.cep.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class LocalCacheService {

    private static final String VALUE_FIELD = "value";
    private static final String EXPIRE_FIELD = "expire";
    private static final Map<String, Map<String, Object>> CACHE = new HashMap<>();

    @Value("${app-config.cache.ttl}")
    private Integer ttl;

    public Mono<String> save(String key, String value) {
        try {
            if(!exists(key)){
                var data = new HashMap<String, Object>();
                data.put(VALUE_FIELD, value);
                data.put(EXPIRE_FIELD, LocalDateTime.now().plusSeconds(ttl));
                CACHE.put(key, data);
                log.info("Salvando cache para chave {}", key);
            }
        } catch (Exception e) {
            log.error("Erro ao tentar salvar cache para chave {}", key, e);
        }
        return Mono.just(value);
    }

    public Mono<String> get(String key) {
        try {
            if(exists(key)) {
                log.info("Cache existente para chave {}", key);
                var data = CACHE.get(key);
                log.info("Retornando cache para chave {}", key);
                return Mono.just((String) data.get(VALUE_FIELD));
            }
            log.info("Cache não encontrado para chave {}", key);
        } catch (Exception e) {
            log.error("Erro ao tentar buscar cache para chave {}", key, e);
        }
        return Mono.empty();
    }

    public Mono<Boolean> existsForKey(String key) {
        return Mono.just(exists(key));
    }

    private boolean exists(String key) {
        var exists = CACHE.containsKey(key);
        if (exists && isExpired((LocalDateTime) CACHE.get(key).get(EXPIRE_FIELD))) {
            remove(key);
            return false;
        }
        return exists;

    }
    
    private boolean isExpired(LocalDateTime expiration) {
        return LocalDateTime.now().isAfter(expiration);

    }

    private void remove(String key) {
        CACHE.remove(key);
    }

    public void removeExpiredKeys() {
        var keysToTemove = CACHE
            .keySet()
            .stream()
            .filter(key -> isExpired((LocalDateTime) CACHE.get(key).get(EXPIRE_FIELD)))
            .collect(Collectors.toList());

            keysToTemove
                .forEach(CACHE::remove);

            if (!keysToTemove.isEmpty()) {
                log.info("{} chaves removidas", keysToTemove.size());
            }
    }
}
