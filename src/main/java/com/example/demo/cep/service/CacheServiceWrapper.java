package com.example.demo.cep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class CacheServiceWrapper {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired 
    private LocalCacheService localCacheService;

    @Value("${app-config.cache.redis-enabled}")
    private Boolean redisEnabbled;

    public Mono<String> get(String key){
        if(redisEnabbled){
            return redisCacheService.get(key);
        }
        return localCacheService.get(key);
    }

    public Mono<Boolean> exists(String key){
        if(redisEnabbled){
            return redisCacheService.existsForKey(key);
        }
        return localCacheService.existsForKey(key);
    }

    public Mono<String> save(String key, String value){
        if(redisEnabbled){
            return redisCacheService.save(key, value);
        }
        return localCacheService.save(key, value);
    }

}
