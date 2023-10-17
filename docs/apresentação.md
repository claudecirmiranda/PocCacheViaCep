Introdução
----------

Nesta apresentação, exploraremos uma solução de autenticação e autorização abrangente, que aborda tanto a autenticação front-end quanto a autenticação back-end. Vamos detalhar o fluxo de funcionamento e destacar o uso do cache para otimizar o desempenho.

Diagrama de sequência do uso da solução:
----------------------------------------

![ds_auth](https://github.com/claudecirmiranda/PocCacheViaCep/assets/70034435/8310f829-7564-4f87-ab22-940a361af0f2)


Fluxo de Funcionamento
----------------------

### Front-end

1.  Fornecimento de Credenciais:
    *   As credenciais de acesso são fornecidas ao cliente para o front-end, incluindo o Tenant-ID, Client-ID, Client-Secret e Callback-URI. A URL de redirecionamento é configurada no aplicativo do Azure AD como single-page ou web, dependendo da necessidade do cliente.
2.  Autenticação MFA:
    *   O front-end inicia o processo de autenticação e realiza a autenticação de vários fatores (MFA) para obter um token do Azure AD.
3.  Solicitação de Token Assinado:
    *   O front-end chama o endpoint `sga/signed-token` da API de autenticação/autorização, enviando o token obtido do Azure AD.
4.  Validação do Token do AD:
    *   A API de autenticação/autorização valida o token do Azure AD e, se a validação for bem-sucedida, gera um token assinado usando uma chave cadastrada no Oracle Cloud Infrastructure Vault. O token assinado é retornado ao front-end.
5.  Cache de Token:
    *   Antes de gerar um novo token assinado, a API de autenticação/autorização verifica se o token assinado já existe no cache. Se estiver no cache, ele é recuperado, caso contrário, um novo token é gerado e salvo no cache.
6.  Solicitação de Lista de Acessos:
    *   O front-end chama o endpoint `sga/getaccess` da API de autenticação/autorização, incluindo o token assinado no cabeçalho como Bearer Token.
7.  Validação da Lista de Acessos:
    *   A API de autenticação/autorização valida o token assinado e, se tudo estiver correto, retorna uma lista de acessos para o front-end.
8.  Cache de Lista de Acessos:
    *   Antes de gerar uma nova lista de acessos, a API de autenticação/autorização verifica se a lista já existe no cache. Se estiver no cache, ela é recuperada, caso contrário, uma nova lista de acessos é gerada e salvo no cache.
9.  Gerenciamento de Acessos:
    *   O front-end itera pela lista de acessos obtida da API de autenticação/autorização e trata o acesso a recursos de acordo com os dados da lista.
10. Tratamento de falhas
    * Qualquer validação que resulte em falha gera tratamentos específicos, como códigos de status HTTP 400, 401 ou 403.

### Back-end

1.  Autenticação Back-end:
    * O back-end realiza a autenticação chamando o endpoint `sga/login` da aplicação de Autenticação/Autorização.
2.  Validação de Credenciais:
    * A API de autenticação/autorização valida as credenciais do usuário e senha recebidas no cabeçalho como basic auth.
3.  Geração de Token Assinado:
    * Se a validação for bem-sucedida, a API de autenticação/autorização gera um token assinado usando uma chave cadastrada no Oracle Cloud Infrastructure Vault e o devolve ao back-end.
4.  Cache de Token:
    * Assim como no front-end, a API de autenticação/autorização verifica se o token assinado já existe no cache antes de gerar um novo, se não existir recupera  e salva no cache.
5.  Chamada à API da Nagem:
    * O back-end chama um endpoint de uma API da Nagem, passando o token assinado no cabeçalho como campo token (exemplo: `https://api2.nagem.com.br/api/sgainteg/getlista1`).
6.  Solicitação de Lista de Acessos:
    * O back-end também chama o endpoint `sga/getaccess` da API de autenticação/autorização, incluindo o token assinado no cabeçalho como Bearer Token.
7.  Validação da Lista de Acessos:
    * A API de autenticação/autorização valida o token assinado e, se tudo estiver correto, retorna a lista de acessos para o back-end.
8.  Cache de Lista de Acessos:
    * Da mesma forma que no front-end, a API de autenticação/autorização verifica se a lista de acessos já existe no cache antes de gerar uma nova, não existindo gera uma nova  e salva no cache.
9.  Gerenciamento de Acessos:
    * O back-end itera pela lista de acessos obtida da API de autenticação/autorização e trata o acesso aos seus próprios endpoints de acordo com os dados da lista.
10. Tratamento de falhas:
    * Assim como no front-end, qualquer validação que resulte em falha gera tratamentos específicos de erro, garantindo a segurança e o controle de acesso.

Conclusão
---------

Nossa solução de autenticação e autorização oferece segurança, controle de acesso e desempenho otimizado tanto para o front-end quanto para o back-end. A integração com o Azure AD e o uso do Oracle Cloud Infrastructure Vault garantem a confiabilidade e a escalabilidade de nossa solução.
Obrigado pela atenção. Estamos à disposição para esclarecer qualquer dúvida que possa surgir.

