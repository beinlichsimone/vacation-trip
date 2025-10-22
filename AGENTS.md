# AGENTS.md — Backend (Spring Boot)

Guia rápido para agentes (humanos e IA) trabalharem com o backend do projeto Vacation Trip.

## Visão e stack

- Java 17, Spring Boot 2.7
- Módulos: Web, Data JPA (Hibernate), Validation, Security (JWT), AMQP/RabbitMQ
- Spring Cloud: OpenFeign, LoadBalancer, Eureka Client
- Banco: PostgreSQL
- Utilitários: ModelMapper, Lombok, Actuator (desabilitado por conflitos com Swagger), Resilience4j, AOP

Estrutura de camadas: Controller → Service → Repository → JPA Entity, com DTOs para I/O e ModelMapper para conversões.

## Como executar

Pré-requisitos: Java 17, Maven, PostgreSQL, RabbitMQ (opcional).

1. Configure o banco (ver Variáveis abaixo).
2. Execute em dev (perfil `dev` abre segurança e CORS):
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
3. Produção (perfil `prod`, exige JWT):
   ```bash
   mvn -DskipTests package
   java -jar target/application.jar --spring.profiles.active=prod
   ```

Servidor padrão: `http://localhost:8083`

## Variáveis e propriedades

Arquivo base: `src/main/resources/application.properties` (e `*-dev.properties`, `*-prod.properties`).

- Porta: `server.port=8083`
- Banco (exemplo):
  - `spring.datasource.url=jdbc:postgresql://localhost:5432/postgres-vacationTrip`
  - `spring.datasource.username=postgres`
  - `spring.datasource.password=postgres`
  - `spring.jpa.hibernate.ddl-auto=update`
- JWT:
  - `vacation-trip.jwt.secret=...`
  - `vacation-trip.jwt.expiration=86400000`
- RabbitMQ: `spring.rabbitmq.queue=ms.voo`

Recomendado: mover segredos para variáveis de ambiente em produção.

## Segurança

- `dev`: `DevSecurityConfiguration` — CORS habilitado, requests livres (GET/POST/PUT/DELETE/OPTIONS)
- `prod`: `SecurityConfiguration` — JWT via `/auth`, filtro `AutenticacaoViaTokenFilter`, regras por endpoint (ex.: GET `/viagem/*` liberado, DELETE exige role `MODERADOR`)

Login: `POST /auth` com `LoginForm` → retorna `TokenDTO` (Bearer).

## Padrões de projeto

- Controllers em `src/main/java/.../controller`
- Services em `.../services`
- Repositories em `.../repository`
- Entidades em `.../model`
- DTOs em `.../dto`
- Configurações em `.../config` (CORS, security, validação)
- Integrações: Feign `.../client/VooClient.java`; RabbitMQ em `RabbitmqService`

## Playbooks (tarefas comuns)

1) Criar novo recurso CRUD
- Model: criar entidade JPA em `model/`
- Repository: interface `JpaRepository` em `repository/`
- DTO: request/response em `dto/`
- Service: regras e orquestração em `services/`
- Controller: endpoints REST em `controller/` (suporte a `Pageable` se preciso)
- Conversões: usar `ModelMapper` via bean em `WebConfig`

2) Paginação
- Adicione `Pageable` no método do controller e `@PageableDefault` se necessário
- `WebConfig` já registra `PageableHandlerMethodArgumentResolver`

3) Proteger endpoints
- Ajuste antMatchers em `SecurityConfiguration` (perfil `prod`)
- Ex.: `.antMatchers(HttpMethod.DELETE, "/recurso/*").hasRole("MODERADOR")`

4) Integração com outro serviço via Feign
- Crie interface com `@FeignClient("nome-servico")`
- Defina métodos com `@RequestMapping` / `@GetMapping`
- Injete no service e trate falhas (pode-se adicionar Resilience4j se necessário)

5) Enviar mensagem RabbitMQ
- Use `RabbitmqService.enviaMensagem(queue, payload)`
- Garanta que a fila exista e RabbitMQ esteja acessível

## Depuração e problemas comuns

- 403 em prod: token ausente/expirado ou perfil errado; ver header `Authorization`
- CORS: ajuste `allowedOrigins` em `WebConfig`
- Conflito Actuator × Swagger: comentários no `application.properties` explicam erro; desabilite Actuator se necessário
- Lazy/EAGER e N+1: revisar `fetch` e `@Fetch(FetchMode.SUBSELECT)` já usado em `Viagem`

## Testes

```bash
mvn test
```

## Convenções

- Validar payloads com Bean Validation nas DTOs
- Não expor entidades diretamente nos controllers; prefira DTOs
- Manter regras de negócio em `services`


