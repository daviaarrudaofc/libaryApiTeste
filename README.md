# Library API

Projeto de estudo em Java e Spring Boot para gerenciamento de livros e autores. A aplicação também reúne uma implementação de Authorization Server OAuth2, emissão de JWT e proteção de endpoints com Spring Security.

## Funcionalidades

- Cadastro, consulta, atualização, exclusão e pesquisa de livros e autores.
- Persistência com Spring Data JPA e PostgreSQL, validação de dados e tratamento de erros da API.
- Cadastro de usuários e clientes OAuth2.
- Login de usuário por formulário e integração de login com Google.
- Authorization Server com os fluxos `client_credentials`, `authorization_code` com PKCE e `refresh_token`.
- Emissão de access tokens JWT assinados com RSA e inclusão das claims `authorities` e `email` quando o principal é um usuário da aplicação.
- Validação de Bearer JWT nas rotas protegidas da própria Library API.

## Tecnologias

Java 21, Spring Boot 4.1.1, Spring Security, Spring Authorization Server, Spring Data JPA, PostgreSQL, Thymeleaf, MapStruct, Lombok e Maven Wrapper.

## Endpoints principais

| Caminho | Papel |
| --- | --- |
| `/autores` e `/livros` | Recursos da API, com operações protegidas por autenticação e permissões. |
| `/usuarios` | Cadastro de usuários. |
| `/clients` | Cadastro de clientes OAuth2. |
| `/login` | Página de autenticação do usuário. |
| `/oauth2/authorize` | Início do fluxo Authorization Code. |
| `/oauth2/token` | Emissão ou renovação de tokens. |
| `/oauth2/jwks` | Chaves públicas usadas para verificar os JWTs. |
| `/authorized` | Callback de estudo que exibe o authorization code recebido. |

Os métodos HTTP, corpos e permissões de cada recurso estão definidos nos controllers e nas configurações de segurança.

## Como funciona a autenticação

O cliente OAuth2 é carregado do banco de dados e pode usar `CLIENT_SECRET_BASIC` para se autenticar no endpoint de token. **Client ID e client secret identificam o cliente; login e senha identificam o usuário.**

- **Client Credentials:** o cliente envia `grant_type=client_credentials` a `/oauth2/token` e recebe um access token para agir em nome próprio, sem login de usuário.
- **Authorization Code com PKCE:** a navegação começa em `/oauth2/authorize`. Após o login, o servidor redireciona para o `redirect_uri` com um código temporário. O cliente troca esse código em `/oauth2/token`, enviando o `code_verifier` associado ao `code_challenge` usado no início do fluxo.
- **Refresh Token:** quando um refresh token é emitido, o cliente pode enviá-lo a `/oauth2/token` com `grant_type=refresh_token` para solicitar outro access token.

No projeto, o access token dura 60 minutos e o refresh token dura 90 minutos. A tela de consentimento está desabilitada; isso não desabilita o PKCE. O `OAuth2TokenCustomizer` adiciona `authorities` e `email` apenas quando o principal é `CustomAuthentication` e o token gerado é um access token.

Ao receber um JWT na própria Library API, o Spring o valida e inicialmente cria um `JwtAuthenticationToken`. O `JwtCustomAuthenticationFilter` tenta localizar o usuário pelo nome do token e, quando o encontra, coloca um `CustomAuthentication` no contexto da requisição. Esse filtro **não gera nem altera o JWT**.

## Projeto complementar: Resource Server

O projeto de teste `resource-server` fica em uma pasta separada da Library API. Ele usa `spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080` e roda na porta `8081`.

No Postman, o teste consiste em obter um access token emitido pela Library API e chamar `GET http://localhost:8081/private` com `Authorization: Bearer <access_token>`. O `resource-server` valida o JWT e, com um token aceito, responde `PRIVATE ENDPOINT OK!`.

Esse experimento demonstra **duas aplicações confiando no mesmo emissor de tokens**. A segunda aplicação protege seu próprio endpoint; ela ainda não faz uma chamada HTTP aos endpoints de livros ou autores da Library API.

Repositório do projeto complementar: [resource_server](https://github.com/daviaarrudaofc/resource_server).

## Executar localmente

1. Tenha Java 21 e PostgreSQL disponíveis.
2. Configure a conexão com o banco em `src/main/resources/application.yml`. Para testar o login com Google, configure também as credenciais dessa integração.
3. Na pasta da Library API, execute `./mvnw.cmd spring-boot:run` no PowerShell. A aplicação usa a porta `8080`.
4. Para o teste entre aplicações, inicie também o projeto `resource-server` com seu próprio `./mvnw.cmd spring-boot:run`. Ele usa a porta `8081`.
5. Obtenha um access token na Library API e envie-o como Bearer para `GET http://localhost:8081/private`.

Para verificar a proteção, compare a resposta de `/private` com token válido, sem token e com token inválido. Antes de atribuir o resultado ao Bearer token, remova também cookies antigos do Postman.

## Testes

Na pasta de cada projeto, execute `./mvnw.cmd test`. Alguns testes da Library API podem depender da configuração local do banco de dados.

## Cuidados antes de publicar

- Não publique senhas de banco, client secrets, access tokens, refresh tokens ou configurações locais com valores reais.
- Revise `application.yml` e o histórico Git antes de enviar novos commits. Se uma credencial já entrou no repositório, **troque-a**; apenas removê-la de um commit novo não a retira do histórico.
- O par de chaves RSA é gerado em memória quando a Library API inicia. Reiniciar a aplicação gera outras chaves; tokens antigos podem deixar de ser verificáveis. Isso serve ao estudo, mas um ambiente persistente exigiria gerenciamento apropriado das chaves.
- Incluir a claim `authorities` no JWT não faz o `resource-server` aplicar automaticamente essas permissões. Para autorização por papel nessa segunda aplicação, é necessário configurar a conversão da claim e as regras de acesso.
