Instruções de Instalação e Execução
===================================

Redis
-----

Para executar o projeto, você precisa ter um servidor Redis em execução. Existem três opções para configurar o Redis:

1.  **Docker (Recomendado):** Você pode iniciar um container Docker com o Redis usando o seguinte comando:
        
    `docker run -p 6379:6379 -t -d redis:5.0.3`
    
    Isso iniciará um servidor Redis na porta 6379 em um container Docker.
    
2.  **Instalação Local:** Você também pode ter o Redis instalado localmente na sua máquina ou em um servidor acessível. Certifique-se de configurar a conexão corretamente no arquivo de configuração da aplicação, se necessário.
    
3.  **Servidor Remoto:** Caso prefira, você pode usar um servidor Redis remoto e configurar a conexão no arquivo de configuração da aplicação.
    

Executar a Aplicação
--------------------

Para executar a aplicação, siga estas etapas:

1.  Certifique-se de que o Redis está em execução e acessível de acordo com a configuração escolhida anteriormente.
    
2.  Abra um terminal na raiz do projeto.
    
3.  Execute o seguinte comando Maven para iniciar a aplicação:
    
    bashCopy code
    
    `mvn spring-boot:run`
    
    Isso iniciará a aplicação Spring Boot.
    
4.  Você pode testar a aplicação abrindo um navegador da web e acessando a seguinte URL:
    [http://localhost:8080/api/v1/cep/30640230](http://localhost:8080/api/v1/cep/30640230)
    Isso irá retornar dados relacionados ao CEP fornecido.
    

Verificar Cache no Redis
------------------------

Se você estiver usando o Redis como mecanismo de cache, pode verificar as chaves armazenadas no Redis da seguinte maneira:

1.  Abra um terminal no seu sistema.
    
2.  Execute o seguinte comando para acessar o terminal do container Docker do Redis:
    
    bashCopy code
    
    `docker exec -it <nome_do_container_redis> /bin/bash`
    
3.  No terminal do container Redis, execute o seguinte comando para listar todas as chaves armazenadas em cache:
    
    bashCopy code
    
    `redis-cli KEYS *`
    
    Isso exibirá uma lista de chaves em cache.
    
4.  Para ver o conteúdo de uma chave específica, execute o seguinte comando, substituindo `<chave>` pelo nome da chave que você deseja visualizar:
    
    bashCopy code
    
    `redis-cli GET "<chave>"`
    
    Isso exibirá o conteúdo da chave específica.
    

Lembre-se de que a estrutura exata pode variar com base na sua configuração e sistema operacional. Certifique-se de adaptar as instruções conforme necessário para o seu ambiente.
