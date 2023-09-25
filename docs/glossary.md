Este glossário resume os principais termos e conceitos relacionados a aplicação da POC, especialmente aqueles relacionados à programação reativa, cache e tecnologias Spring utilizadas.

1.  **Spring Boot**:
    *   Um projeto do ecossistema Spring que simplifica o desenvolvimento de aplicativos Spring, fornecendo convenções e configurações padrão.
2.  **Spring WebFlux**:
    *   Um projeto do ecossistema Spring que oferece suporte à programação reativa, permitindo a construção de aplicativos reativos e escaláveis.
3.  **WebClient**:
    *   Uma classe do Spring WebFlux usada para fazer chamadas assíncronas a serviços externos, como APIs HTTP.
4.  **Cliente e Método Reativo**:
    *   A implementação de um cliente que realiza chamadas assíncronas usando WebClient e a conversão do resultado em um objeto reativo, como Mono.
5.  **Mono**:
    *   Um tipo reativo do Spring WebFlux que representa uma única unidade de resultado, que pode ser um valor ou uma exceção, e é usado para representar resultados reativos em operações assíncronas.
6.  **Cache**:
    *   Um mecanismo de armazenamento temporário de dados usado para melhorar o desempenho e reduzir a carga em serviços externos, armazenando resultados previamente obtidos.
7.  **Redis**:
    *   Um sistema de armazenamento de chave-valor em memória que é frequentemente usado como uma solução de cache de alto desempenho.
8.  **Componente Spring**:
    *   Um componente gerenciado pelo Spring Framework, geralmente anotado com `@Component`, que é criado e gerenciado pelo contêiner Spring.
9.  **Repositório Spring Data**:
    *   Um componente usado para acessar dados em bancos de dados ou outros sistemas de armazenamento de dados, que geralmente envolve operações CRUD (Create, Read, Update, Delete).
10.  **TTL (Time-to-Live)**:
    *   Um tempo de vida associado a um objeto em um sistema de cache que determina por quanto tempo o objeto deve ser mantido em cache antes de ser considerado obsoleto e removido.
11.  **Programação Funcional Reativa**:
    *   Um paradigma de programação que lida com fluxos de dados assíncronos e eventos reativos, permitindo operações assíncronas e não bloqueantes.
12.  **API ViaCep**:
    *   Uma API externa usada para consultar informações de endereço com base em CEPs fornecidos.
13.  **ADR (Architecture Decision Record)**:
    *   Um documento que registra decisões arquiteturais importantes tomadas durante o desenvolvimento de um sistema ou aplicativo, documentando o contexto, as decisões e as justificativas.
