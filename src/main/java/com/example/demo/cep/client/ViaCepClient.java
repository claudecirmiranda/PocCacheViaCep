package com.example.demo.cep.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ViaCepClient {
    
    @Autowired
    private WebClient webClient;

    @Value("${app-config.client.via-cep}")
    private String viaCepUri;

    public Mono<String> findByCep(String cep) {

        log.info("Buscando dados da API ViaCep para o CEP {}", cep);

        return webClient
            .get()
            .uri(buildUri(cep))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(throwable -> {
                log.error("Erro ao chamar a API do ViaCep para o CEP {}", cep, throwable);
                return Mono.empty();
            })
            .doOnNext(response -> log.info("Retornando dados da API do ViaCep para o CEP {}", cep, response));
        
    }

    public String buildUri(String cep) {
        log.info("URL: {}", String.format(viaCepUri, cep));
        return String.format(viaCepUri, cep);
    }
    
}
