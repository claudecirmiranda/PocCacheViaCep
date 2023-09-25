ADR: Decisões de Arquitetura da POC para Aplicação Spring WebFlux de Consulta de CEPs para contexto de Cache
==============================================================================

Contexto
--------

Nessa POC, a aplicação é um serviço de consulta de CEPs que fornece informações de endereço com base em CEPs fornecidos pelos clientes. A aplicação é construída com o Spring WebFlux, que oferece suporte a programação reativa. A arquitetura precisa considerar o uso do WebClient para chamadas assíncronas a serviços externos, o uso de Mono como tipo reativo para representar resultados e a implementação de cache.

Decisões Arquiteturais
----------------------

### 1\. Uso do Spring WebFlux WebClient

Decidimos usar o WebClient do Spring WebFlux para fazer chamadas assíncronas a serviços externos, como a API ViaCep, a fim de aproveitar a natureza reativa do Spring WebFlux. Isso permite que nossa aplicação seja escalável e eficiente em termos de recursos ao lidar com solicitações externas.

### 2\. Cliente e Método Reativo

Optamos por criar um cliente reativo (`ViaCepClient`) para encapsular a lógica de comunicação com a API do ViaCep. Isso permite que as chamadas sejam reativas e assíncronas, adequadas para o Spring WebFlux. Além disso, usamos o método `bodyToMono` para converter a resposta em um objeto Mono, tornando o resultado reativo e compatível com o fluxo de dados da aplicação.

### 3\. Uso do Tipo Reativo Mono

Escolhemos o tipo reativo Mono para representar resultados em nossa aplicação. O Mono representa uma única unidade de resultado, seja um valor ou uma exceção, e é adequado para a programação reativa, pois permite o processamento assíncrono e a manipulação de eventos reativos.

### 4\. Implementação de Cache

Implementamos uma camada de cache para melhorar o desempenho e reduzir a carga nos serviços externos, como o serviço ViaCep. A aplicação oferece suporte a dois tipos de cache: Redis e cache local. A decisão de usar Redis ou cache local é determinada pela configuração definida no arquivo de propriedades (`app-config.cache.redis-enabled`). O serviço `CacheServiceWrapper` age como um invólucro para rotear operações de cache para o serviço correto com base nessa configuração.

Conclusão
---------

Essas decisões arquiteturais garantem que nossa aplicação seja altamente responsiva, escalável e eficiente em termos de recursos. O uso do Spring WebFlux e do WebClient permite chamadas assíncronas, enquanto o Mono representa resultados de maneira reativa. A implementação de cache ajuda a otimizar o desempenho ao reduzir a necessidade de buscar dados repetidamente em serviços externos. Essas escolhas arquiteturais são fundamentais para criar uma aplicação robusta e eficaz na consulta de informações de CEPs.


**WebClientConfig**

A classe `WebClientConfig` é uma classe de configuração Spring que define e configura um bean do WebClient, que é usado para fazer chamadas HTTP reativas. Aqui está uma análise detalhada dessa classe:

1.  **Pacote e Imports**:
    *   A classe está localizada no pacote `com.example.demo.config`.
    *   Ela importa classes relacionadas à configuração do Spring Framework e ao WebClient.
2.  **Annotations**:
    *   A classe é anotada com `@Configuration`, indicando que é uma classe de configuração Spring.
3.  **Método `webClient`**:
    *   Este método é anotado com `@Bean`, indicando que ele é um método de configuração Spring que produz um bean gerenciado pelo Spring.
    *   Dentro do método, um objeto WebClient é criado e configurado.
    *   O método `builder()` é usado para obter um construtor do WebClient.
    *   O método `build()` é chamado para criar uma instância final do WebClient com as configurações padrão.

Essencialmente, esta classe `WebClientConfig` configura um bean do WebClient sem personalizações específicas. O WebClient é uma parte importante do Spring WebFlux e é usado para fazer chamadas HTTP reativas para outros serviços ou APIs. Este bean pode ser injetado em outras partes do código onde for necessário fazer chamadas HTTP reativas. Se for necessário personalizar a configuração do WebClient, como definir um tempo limite ou configurar um cliente HTTP específico, essas personalizações podem ser feitas neste método `webClient`. Caso contrário, ele usa as configurações padrão do WebClient.

**RedisConfig**

A classe `RedisConfig` é uma classe de configuração do Spring que se concentra na configuração do Redis para operações reativas. Vamos analisar os principais aspectos desta classe:

1.  **Pacote e Imports**:
    *   O código está organizado no pacote `com.example.demo.config`.
    *   Ele importa várias classes do Spring Framework relacionadas à configuração do Redis.
2.  **Annotations**:
    *   A classe é anotada com `@Configuration`, indicando que ela é uma classe de configuração Spring.
3.  **Método `redisOperations`**:
    *   Este método é anotado com `@Bean`, o que significa que ele é um método de configuração Spring que produz um bean gerenciado pelo Spring.
    *   Ele recebe um parâmetro `factory` do tipo `ReactiveRedisConnectionFactory`, que é injetado automaticamente pelo Spring.
4.  **Configuração do Serializador**:
    *   A classe utiliza um `Jackson2JsonRedisSerializer` para serializar valores em formato JSON.
    *   É criado um `RedisSerializationContextBuilder` com um `StringRedisSerializer` para a chave.
    *   O contexto de serialização é configurado com o serializador JSON para os valores e o serializador de strings para as chaves.
5.  **Criação do Bean `ReactiveRedisTemplate`**:
    *   Um bean do tipo `ReactiveRedisTemplate` é criado com base na fábrica `factory` e no contexto de serialização configurado.
    *   Este bean é retornado como resultado do método.

Em resumo, esta classe `RedisConfig` é responsável por configurar um `ReactiveRedisTemplate` personalizado, que permite a interação reativa com um servidor Redis. Ele usa um serializador JSON para armazenar valores em formato JSON e um serializador de strings para as chaves. O `ReactiveRedisConnectionFactory` é injetado automaticamente e usado para criar o `ReactiveRedisTemplate`. Essa configuração é útil quando você deseja personalizar a forma como os dados são armazenados e recuperados de um servidor Redis em um ambiente reativo no Spring Framework.

**RedisCacheService**

Este código é uma classe Java que faz parte de um serviço de cache baseado no Spring Framework. Vamos analisar os detalhes arquiteturais e outros aspectos relevantes:

1.  **Pacote e Imports**:
    *   O código está organizado no pacote `com.example.demo.cep.service`.
    *   Ele importa classes do Spring Framework (`@Service` e `@Autowired`) e outras dependências necessárias (`CacheRepository`, `Mono`, `Slf4j`).
2.  **Annotations**:
    *   A classe é anotada com `@Service`, indicando que é um componente gerenciado pelo Spring e pode ser injetado em outras partes do código.
    *   Também utiliza a anotação `@Slf4j`, que gera um logger SLF4J chamado `log`.
3.  **Injeção de Dependência**:
    *   A classe `RedisCacheService` possui uma injeção de dependência do tipo `CacheRepository` por meio da anotação `@Autowired`. Isso significa que o `CacheRepository` é automaticamente injetado na classe, permitindo que ela o utilize.
4.  **Método `save`**:
    *   Este método recebe uma chave (`key`) e um valor (`value`) como parâmetros.
    *   Tenta salvar o valor no cache por meio do `cacheRepository`.
    *   Usa uma abordagem reativa, onde o resultado da operação é encapsulado em um `Mono`.
    *   Se a operação de salvamento for bem-sucedida, um log é gerado usando SLF4J.
    *   Erros durante o salvamento são capturados e registrados em logs.
5.  **Método `get`**:
    *   Este método recebe uma chave (`key`) como parâmetro.
    *   Tenta recuperar o valor do cache associado à chave por meio do `cacheRepository`.
    *   Também retorna o resultado em um `Mono`.
    *   Gera um log informando que está retornando o valor do cache.
6.  **Método `existsForKey`**:
    *   Este método verifica se existe um valor no cache associado à chave fornecida.
    *   Retorna um `Mono` que emite um valor booleano indicando a existência.
    *   Gera logs informando se o cache existe ou não.
7.  **Tratamento de Exceções**:
    *   Todos os métodos capturam exceções possíveis (do tipo `Exception`) e registram mensagens de erro em logs, incluindo detalhes sobre a chave e exceção.
8.  **Retorno Padrão**:
    *   Em caso de exceção, os métodos retornam um `Mono` com um valor padrão (geralmente vazio ou `false`).
9.  **Reatividade**:
    *   A classe utiliza a programação reativa com o uso de `Mono` para representar resultados assíncronos.

Em resumo, esta classe `RedisCacheService` é responsável por fornecer métodos assíncronos para salvar, recuperar e verificar a existência de valores no cache. Ela usa a injeção de dependência do Spring para acessar o `CacheRepository` e registra logs para monitorar o comportamento do serviço de cache. Note que esta análise assume que os métodos do `cacheRepository` funcionam conforme o esperado e que a configuração do cache está correta em outros lugares do código.

**LocalCacheService**

A classe `LocalCacheService` é responsável por fornecer funcionalidades de cache local para a aplicação. Ela mantém um cache em memória usando um `Map` para armazenar os dados e gerencia a expiração dos itens no cache. Abaixo está uma análise detalhada desta classe:

1.  **Pacote e Imports**:
    *   A classe `LocalCacheService` está localizada no pacote `com.example.demo.cep.service`.
    *   Ela importa classes relacionadas ao Spring Framework, como `Service`, bem como classes de logging do projeto Lombok (`@Slf4j`).
2.  **Annotations**:
    *   A classe é anotada com `@Service`, indicando que é um componente gerenciado pelo Spring.
    *   Ela faz uso da anotação `@Slf4j` para gerar um logger SLF4J chamado `log`.
3.  **Campos**:
    *   A classe possui os seguintes campos e constantes:
        *   `VALUE_FIELD` e `EXPIRE_FIELD`: Constantes usadas como chaves para os campos do cache.
        *   `CACHE`: Um `Map` que armazena os dados em cache. A chave é uma `String` (a chave do cache) e o valor é um `Map` com campos `value` e `expire`.
4.  **Injeção de Dependência**:
    *   A classe não faz uso de injeção de dependência de outros componentes ou serviços, pois o cache é mantido localmente em memória.
5.  **Método `save`**:
    *   Este método recebe uma chave (`key`) e um valor (`value`) como parâmetros.
    *   Ele verifica se a chave já existe no cache usando o método `exists`.
    *   Se a chave não existir, ele cria um novo mapa de dados, armazena o valor e define um tempo de expiração com base na configuração `ttl`.
    *   Registra uma mensagem de log indicando que o cache foi salvo.
6.  **Método `get`**:
    *   Este método recebe uma chave (`key`) como parâmetro.
    *   Ele verifica se a chave existe no cache usando o método `exists`.
    *   Se a chave existir e não tiver expirado, ele recupera o valor associado à chave e o retorna em um `Mono`.
    *   Registra mensagens de log indicando se o cache foi encontrado ou não.
7.  **Método `existsForKey`**:
    *   Este método recebe uma chave (`key`) como parâmetro e retorna um `Mono` com um valor booleano indicando se a chave existe ou não no cache.
8.  **Método `exists`**:
    *   Este método verifica se a chave existe no cache e se o item associado à chave não expirou.
    *   Se a chave existir, mas o item estiver expirado, ele é removido e o método retorna `false`.
9.  **Método `isExpired`**:
    *   Este método verifica se um determinado tempo de expiração (representado por um objeto `LocalDateTime`) já passou.
10.  **Método `remove`**:
    *   Este método remove um item do cache com base em sua chave.
11.  **Método `removeExpiredKeys`**:
    *   Este método percorre o cache e remove todas as chaves cujos itens expiraram.
    *   Registra uma mensagem de log indicando quantas chaves foram removidas.

No geral, a classe `LocalCacheService` oferece funcionalidades de cache local para armazenar dados em memória e gerenciar sua expiração. Ela é responsável por salvar, buscar e verificar a existência de dados em cache, bem como remover itens expirados. Essa classe é uma alternativa leve para o uso de caches mais robustos, como o Redis, quando a necessidade de cache é simples e não requer compartilhamento entre várias instâncias da aplicação.

**CacheRepository**  

A classe CacheRepository é uma parte fundamental da implementação do cache usando Redis em sua aplicação. Ela atua como um repositório para realizar operações de salvamento, recuperação e verificação de existência de dados no Redis. Aqui está uma análise detalhada dessa classe:

Pacote e Imports:

A classe CacheRepository está localizada no pacote com.example.demo.repository.
Ela importa várias classes relacionadas ao Spring Framework e ao Spring Data Redis, bem como recursos relacionados a programação reativa.
Annotations:

A classe é anotada com @Repository, indicando que é um componente gerenciado pelo Spring e que trata o acesso a dados (neste caso, o Redis).
Injeção de Dependência:

A classe injeta um ReactiveRedisTemplate<String, String> para acessar o Redis de forma reativa. Esse objeto é usado para realizar operações CRUD no Redis.
Campos e Configurações:

A classe também injeta o valor de app-config.cache.ttl do arquivo de propriedades para definir o tempo de vida (TTL) dos dados no Redis.
O campo ttl é usado para determinar o tempo de vida dos dados no cache.
Método save:

Este método é usado para salvar dados no Redis.
Ele usa o ReactiveRedisTemplate para definir o valor associado à chave especificada no Redis.
Após definir o valor, ele também define o tempo de vida (TTL) para essa chave usando o método expire, garantindo que os dados tenham uma data de expiração.
Em caso de erro durante o salvamento, o método registra um erro no log e retorna um Mono com valor false.
Método get:

Este método é usado para recuperar dados do Redis com base em uma chave.
Ele usa o ReactiveRedisTemplate para buscar o valor associado à chave especificada no Redis.
Em caso de erro durante a recuperação, o método registra um erro no log e retorna um Mono vazio (Mono.empty()).
Método existsForKey:

Este método é usado para verificar a existência de uma chave no Redis.
Ele usa o ReactiveRedisTemplate para verificar se a chave existe.
Em caso de erro durante a verificação, o método registra um erro no log e retorna um Mono com valor false.
No geral, a classe CacheRepository é responsável por interagir com o Redis e implementar operações de cache, como salvar, recuperar e verificar a existência de dados. Essa classe é crucial para a implementação de cache eficaz e eficiente em sua aplicação, permitindo o armazenamento temporário de dados e melhorando o desempenho ao evitar chamadas repetidas a serviços externos.

**ViaCepClient**

A classe `ViaCepClient` é uma classe que atua como um cliente reativo para a API ViaCep. Ela é responsável por buscar informações de endereços com base em CEPs usando o WebClient do Spring WebFlux. Abaixo está uma análise detalhada desta classe:

1.  **Pacote e Imports**:
    *   A classe está localizada no pacote `com.example.demo.cep.client`.
    *   Ela importa várias classes do Spring Framework relacionadas à criação de clientes reativos.
2.  **Annotations**:
    *   A classe é anotada com `@Component`, indicando que é um componente gerenciado pelo Spring e pode ser injetado em outras partes do código.
    *   Ela também utiliza a anotação `@Slf4j` para gerar um logger SLF4J chamado `log`.
3.  **Injeção de Dependência**:
    *   A classe `ViaCepClient` possui duas injeções de dependência:
        *   Um `WebClient` é injetado usando `@Autowired`, permitindo que a classe faça chamadas HTTP de forma reativa.
        *   Uma URL da API ViaCep é injetada usando a anotação `@Value`, recuperando o valor da propriedade `app-config.client.via-cep` do arquivo de configuração.
4.  **Método `findByCep`**:
    *   Este método recebe um CEP como parâmetro.
    *   Utiliza o WebClient para fazer uma solicitação GET à API ViaCep, usando a URI construída com base no CEP.
    *   Utiliza métodos reativos, como `retrieve()` e `bodyToMono()`, para recuperar a resposta da API como uma string em formato JSON.
    *   Trata erros usando `onErrorResume()`, registrando mensagens de erro em logs e retornando um `Mono` vazio em caso de erro.
    *   Gera logs para indicar o início da busca e o retorno dos dados da API.
5.  **Método `buildUri`**:
    *   Este método constrói a URI da API ViaCep com base no CEP fornecido.
    *   Registra a URL gerada em logs.

Em resumo, a classe `ViaCepClient` é responsável por fazer chamadas reativas à API ViaCep para buscar informações com base em CEPs. Ela usa o WebClient do Spring WebFlux para realizar as chamadas HTTP de forma assíncrona e trata erros de forma apropriada. Além disso, ela registra logs para rastrear o fluxo de execução e os resultados das chamadas à API. Esta classe é um exemplo de como criar um cliente reativo para integração com serviços externos em um aplicativo Spring WebFlux.

**CepService**

A classe `CepService` é responsável por buscar informações de endereço com base em um CEP. Ela faz uso de um serviço de cache (representado por `cacheServiceWrapper`) para otimizar a recuperação de dados, e também utiliza o `ViaCepClient` para fazer chamadas à API ViaCep. Vamos analisar os principais aspectos dessa classe:

1.  **Pacote e Imports**:
    *   A classe está localizada no pacote `com.example.demo.cep.service`.
    *   Ela importa várias classes relacionadas ao Spring Framework, ao cliente `ViaCepClient`, ao `CepResponse` e ao `ObjectMapper` para serialização/desserialização de objetos.
2.  **Annotations**:
    *   A classe é anotada com `@Service`, indicando que é um componente gerenciado pelo Spring.
    *   Ela também utiliza a anotação `@Slf4j` para gerar um logger SLF4J chamado `log`.
3.  **Injeção de Dependência**:
    *   A classe `CepService` faz uso de injeção de dependência para acessar outros componentes:
        *   `ViaCepClient`: Usado para fazer chamadas à API ViaCep.
        *   `ObjectMapper`: Utilizado para desserializar a resposta da API em um objeto `CepResponse`.
        *   `CacheServiceWrapper`: Usado para verificar e gerenciar o cache dos resultados das chamadas à API.
4.  **Método `findByCep`**:
    *   Este método recebe um CEP como parâmetro e retorna um `Mono` de `CepResponse`.
    *   Ele começa verificando se os dados para o CEP estão no cache usando o `cacheServiceWrapper.exists(cep)`.
    *   Se os dados estiverem no cache, ele os recupera usando `cacheServiceWrapper.get(cep)`.
    *   Se os dados não estiverem no cache, ele chama o método `findByCep` do `ViaCepClient` para buscar os dados da API ViaCep.
    *   Após obter a resposta da API, ele a armazena no cache usando `cacheServiceWrapper.save(cep, response)`.
    *   Finalmente, ele chama o método `handleResponse` para converter a resposta em um objeto `CepResponse`.
5.  **Método `handleResponse`**:
    *   Este método recebe uma resposta como uma string.
    *   Ele verifica se a resposta não está vazia.
    *   Se a resposta não estiver vazia, tenta desserializá-la em um objeto `CepResponse` usando o `ObjectMapper`.
    *   Em caso de sucesso na desserialização, retorna o objeto `CepResponse` encapsulado em um `Mono`.
    *   Se ocorrer algum erro durante a desserialização, registra uma mensagem de erro em logs e retorna um `Mono` vazio.

No geral, a classe `CepService` é responsável por orquestrar a busca de informações de endereço com base em CEPs. Ela otimiza a recuperação de dados usando um serviço de cache, reduzindo assim a carga na API ViaCep. Além disso, lida com a desserialização da resposta da API em um objeto `CepResponse` para facilitar o uso dos dados recuperados.

**CepController**

A classe `CepController` é um controlador Spring que lida com solicitações relacionadas a CEPs. Ela usa o serviço `CepService` para buscar informações de endereço com base em um CEP e retorna essas informações como resposta. Abaixo está uma análise detalhada desta classe:

1.  **Pacote e Imports**:
    *   A classe está localizada no pacote `com.example.demo.cep.controller`.
    *   Ela importa várias classes relacionadas ao Spring Framework e à lógica de serviço `CepService`, bem como a classe `CepResponse` para representar os dados de resposta.
2.  **Annotations**:
    *   A classe é anotada com `@RestController`, indicando que é um controlador Spring que lida com solicitações REST.
    *   Ela também usa a anotação `@RequestMapping` para mapear todos os endpoints deste controlador para o caminho `/api/v1/cep`.
3.  **Injeção de Dependência**:
    *   A classe `CepController` faz uso de injeção de dependência para acessar o serviço `CepService`. O serviço é injetado usando `@Autowired`.
4.  **Método `findByCep`**:
    *   Este método é anotado com `@GetMapping("{cep}")`, indicando que ele lida com solicitações GET para o caminho `/api/v1/cep/{cep}`.
    *   Ele espera um parâmetro de caminho `{cep}` que é automaticamente vinculado ao parâmetro do método `cep`.
    *   O método chama o serviço `CepService` para buscar informações de endereço com base no CEP fornecido.
    *   O resultado é encapsulado em um `Mono` de `CepResponse` que representa a resposta reativa.

Em resumo, a classe `CepController` é responsável por expor um endpoint REST que permite a busca de informações de endereço com base em um CEP. Ele delega a lógica de busca para o serviço `CepService`, que retorna um objeto `CepResponse`. Essa classe é um componente fundamental em uma aplicação Spring que oferece uma API REST para consultar informações de CEPs de forma reativa.

**CepResponse**

A classe `CepResponse` é uma classe DTO (Data Transfer Object) que representa a resposta contendo informações de endereço associadas a um CEP. Aqui está uma análise detalhada desta classe:

1.  **Pacote e Imports**:
    *   A classe está localizada no pacote `com.example.demo.cep.dto`.
    *   Ela importa as anotações `@Data`, `@NoArgsConstructor` e `@AllArgsConstructor` do projeto Lombok.
2.  **Anotações Lombok**:
    *   A classe faz uso das anotações do Lombok para gerar automaticamente métodos comuns, como getters, setters, `equals()`, `hashCode()`, e `toString()`. Veja o significado de cada anotação:
        *   `@Data`: Gera automaticamente os métodos `getters`, `setters`, `equals`, `hashCode` e `toString` para todos os campos da classe.
        *   `@NoArgsConstructor`: Gera um construtor padrão sem argumentos.
        *   `@AllArgsConstructor`: Gera um construtor que aceita todos os campos da classe como argumentos.
3.  **Campos**:
    *   A classe `CepResponse` possui os seguintes campos para representar as informações de endereço associadas a um CEP:
        *   `cep`: Representa o código postal (CEP).
        *   `logradouro`: Representa o logradouro (rua, avenida, etc.).
        *   `complemento`: Representa informações adicionais de endereço (por exemplo, número do prédio, bloco, etc.).
        *   `bairro`: Representa o bairro.
        *   `localidade`: Representa a cidade ou localidade.
        *   `uf`: Representa o estado (Unidade da Federação).
4.  **Construtores**:
    *   A classe possui dois construtores gerados automaticamente pelo Lombok:
        *   Um construtor padrão sem argumentos (construtor vazio).
        *   Um construtor que aceita todos os campos da classe como argumentos.
5.  **Métodos Adicionais**:
    *   Além dos métodos gerados automaticamente pelo Lombok, não há métodos personalizados definidos nesta classe. Ela é principalmente usada para representar dados e é geralmente serializada para JSON em uma resposta de API REST.

Em resumo, a classe `CepResponse` é uma classe simples que serve como uma representação de dados para informações de endereço associadas a um CEP. Ela usa o projeto Lombok para gerar automaticamente métodos comuns e simplificar a definição da classe. Essa classe é comumente usada para serializar dados de resposta de uma API REST que fornece informações de endereço com base em um CEP.

**CacheServiceWrapper**

A classe `CacheServiceWrapper` é um serviço que atua como um invólucro para a funcionalidade de cache. Ela é projetada para direcionar operações de cache para um serviço de cache específico com base em uma configuração definida no arquivo de propriedades. Abaixo está uma análise detalhada desta classe:

1.  **Pacote e Imports**:
    *   A classe está localizada no pacote `com.example.demo.cep.service`.
    *   Ela importa várias classes relacionadas ao Spring Framework, bem como os serviços de cache `RedisCacheService` e `LocalCacheService`.
2.  **Annotations**:
    *   A classe é anotada com `@Service`, indicando que é um componente gerenciado pelo Spring.
3.  **Injeção de Dependência**:
    *   A classe `CacheServiceWrapper` faz uso de injeção de dependência para acessar outros serviços relacionados ao cache:
        *   `RedisCacheService`: Usado para operações de cache com Redis.
        *   `LocalCacheService`: Usado para operações de cache local.
    *   Também injeta uma variável booleana `redisEnabled` que indica se o cache Redis está habilitado ou desabilitado com base na configuração definida no arquivo de propriedades.
4.  **Método `get`**:
    *   Este método recebe uma chave (`key`) como parâmetro.
    *   Ele verifica se o cache Redis está habilitado (`redisEnabled`).
    *   Se estiver habilitado, chama o método `get` do `RedisCacheService` para recuperar o valor do cache.
    *   Se estiver desabilitado, chama o método `get` do `LocalCacheService`.
5.  **Método `exists`**:
    *   Este método recebe uma chave (`key`) como parâmetro.
    *   Ele verifica se o cache Redis está habilitado (`redisEnabled`).
    *   Se estiver habilitado, chama o método `existsForKey` do `RedisCacheService` para verificar se a chave existe no cache.
    *   Se estiver desabilitado, chama o método `existsForKey` do `LocalCacheService` para fazer a mesma verificação.
6.  **Método `save`**:
    *   Este método recebe uma chave (`key`) e um valor (`value`) como parâmetros.
    *   Ele verifica se o cache Redis está habilitado (`redisEnabled`).
    *   Se estiver habilitado, chama o método `save` do `RedisCacheService` para salvar a chave e o valor no cache Redis.
    *   Se estiver desabilitado, chama o método `save` do `LocalCacheService` para salvar a chave e o valor no cache local.

Esse wrapper garante que as operações de cache sejam roteadas para o serviço correto com base na configuração do aplicativo, baseado na configuração `app-config.cache.redis-enabled` definida no arquivo de propriedades, para refletir a escolha entre o uso do Redis ou do cache local.

**CacheScheduler**

A classe `CacheScheduler` é responsável por agendar e executar uma tarefa de limpeza de chaves expiradas no cache. Vamos analisar os principais elementos desta classe:

1.  **Pacote e Imports**:
    *   A classe `CacheScheduler` está localizada no pacote `com.example.demo.cep.service`.
    *   Ela importa as anotações e dependências necessárias do Spring Framework.
2.  **Anotação `@Component`**:
    *   A classe é anotada com `@Component`, indicando que ela é um componente gerenciado pelo Spring e deve ser detectada automaticamente durante a inicialização da aplicação.
3.  **Injeção de Dependência**:
    *   A classe injeta um bean do tipo `LocalCacheService` usando a anotação `@Autowired`. Isso permite que o `CacheScheduler` acesse e chame métodos do `LocalCacheService`.
4.  **Método `removeExpiredKeys`**:
    *   Este é o método principal da classe e é anotado com `@Scheduled(cron = "${app-config.cache.scheduler}")`. Essa anotação indica que o método deve ser executado periodicamente com base na expressão cron especificada no arquivo de configuração (provavelmente em `application.properties` ou `application.yml`).
    *   O método chama `localCacheService.removeExpiredKeys()`, que é responsável por remover chaves expiradas do cache local. Isso ajuda a manter o cache limpo e evitar que ele cresça indefinidamente.

No geral, o `CacheScheduler` desempenha um papel importante na manutenção do cache local, garantindo que chaves expiradas sejam removidas de forma programada. Isso contribui para a eficiência e o bom funcionamento do sistema de cache em sua aplicação. Certifique-se de que a expressão cron definida no arquivo de configuração esteja configurada de acordo com os requisitos de limpeza do cache da sua aplicação.

**Cache Reativo**

No contexto de caching reativo, que faz uso do cache reativo do Spring Framework (usado em conjunto com o Spring WebFlux), a anotação `@EnableCaching` não é necessária. Isso ocorre porque o caching reativo funciona de maneira um pouco diferente do caching tradicional do Spring (usado com o Spring MVC), e a ativação do caching é tratada de forma automática.
Aqui está o motivo:

1.  **Caching Tradicional (Imperativo)**:
    *   No caching tradicional do Spring (usado com o Spring MVC), você normalmente usa a anotação `@EnableCaching` para habilitar o caching na aplicação.
    *   Em seguida, você pode usar anotações como `@Cacheable`, `@CacheEvict` e `@CachePut` para controlar o comportamento de caching de métodos específicos.
2.  **Caching Reativo**:
    *   No caching reativo, que é comumente usado com o Spring WebFlux, você não precisa da anotação `@EnableCaching`.
    *   O Spring WebFlux faz uso de um mecanismo de caching reativo que não requer essa ativação explícita.
    *   Você ainda pode usar anotações como `@Cacheable` e outras, mas elas são fornecidas pelo módulo reativo do Spring.

Portanto, se você deseja trabalhar com o Spring WebFlux e aproveitar o caching reativo, não é necessário incluir `@EnableCaching` em sua configuração. O caching reativo será ativado automaticamente no contexto do Spring WebFlux, e você pode usar anotações de caching reativo para controlar o comportamento de caching de seus métodos. Certifique-se de configurar adequadamente seu mecanismo de cache (como o Redis) para funcionar com o Spring WebFlux, conforme foi demonstrado nas classes de configuração e dependências.
