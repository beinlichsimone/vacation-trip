## Vacation Trip — Backend (Spring Boot)

API REST para gestão de viagens, pessoas, passeios, documentos e deslocamentos. Implementada em Spring Boot 2.7 (Java 17), com segurança JWT (em produção), PostgreSQL, integrações via OpenFeign e mensageria opcional via RabbitMQ.

### Visão geral

- Porta padrão: `http://localhost:8083`
- Perfis: `dev` (segurança aberta para desenvolvimento) e `prod` (JWT obrigatório)
- Frontend complementar: Next.js em `frontend/` (com proxy `/api` → backend)

### Stack e arquitetura

- Spring Boot: Web, Data JPA (Hibernate), Validation, Security (JWT)
- Banco de dados: PostgreSQL
- Integrações: Spring Cloud OpenFeign, LoadBalancer, Eureka Client (opcional)
- Mensageria: Spring AMQP/RabbitMQ (opcional)
- Utilitários: ModelMapper, Lombok, Actuator (comentado para evitar conflito com Swagger), Resilience4j, AOP
- Arquitetura em camadas: Controller → Service → Repository → JPA Entity, com DTOs para I/O

### Requisitos

- Java 17, Maven 3.8+
- PostgreSQL (local ou remoto)
- RabbitMQ (opcional, apenas se for utilizar a fila)

### Como rodar

#### Desenvolvimento (perfil `dev`)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

- CORS habilitado para `http://localhost:3000`
- Endpoints liberados sem autenticação

#### Produção (perfil `prod`)

```bash
mvn -DskipTests package
java -jar target/application.jar --spring.profiles.active=prod
```

- Requer login (`POST /auth`) e uso de token Bearer em chamadas protegidas

### Configuração

Defina as propriedades em `src/main/resources/application.properties` ou nos específicos por perfil (`application-dev.properties`, `application-prod.properties`).

```properties
server.port=8083

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres-vacationTrip
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# JWT
vacation-trip.jwt.secret=troque-este-valor-em-producao
vacation-trip.jwt.expiration=86400000

# RabbitMQ (opcional)
spring.rabbitmq.queue=ms.voo
```

Recomendado: em produção, usar variáveis de ambiente para segredos (senha do DB, `vacation-trip.jwt.secret`).

### Segurança (JWT no perfil `prod`)

- `DevSecurityConfiguration` (perfil `dev`): CORS habilitado, todas as rotas liberadas.
- `SecurityConfiguration` (perfil `prod`):
  - `POST /auth` liberado para login.
  - `GET /viagem/*` liberado.
  - `DELETE /viagem/*` exige role `MODERADOR`.
  - Demais rotas autenticadas.
  - Filtro `AutenticacaoViaTokenFilter` valida JWT em cada request.

Fluxo de autenticação (exemplo):

```bash
curl -X POST http://localhost:8083/auth \
  -H "Content-Type: application/json" \
  -d '{"email":"usuario@exemplo.com","password":"senha"}'
# resposta: { "token": "<jwt>", "tipo": "Bearer" }

curl http://localhost:8083/viagem/viagens \
  -H "Authorization: Bearer <jwt>"
```

### Endpoints (exemplos)

- `GET /viagem/viagens` — lista paginada de viagens (suporta `Pageable`)
- `GET /viagem/{id}` — detalhe
- `POST /viagem`, `PUT /viagem/{id}`, `DELETE /viagem/{id}`
- Recursos similares: `pessoa`, `passeio`, `documento`, `deslocamento`

Obs.: Controllers residem em `src/main/java/.../controller`. DTOs em `.../dto`.

### Banco de dados

- JPA/Hibernate com `ddl-auto=update` em desenvolvimento.
- Arquivo `src/main/resources/data.sql` pode inserir dados iniciais.

### Integrações

- Feign (`client/VooClient.java`): comunicação com serviço "voo" (descoberta via Eureka quando habilitado).
- RabbitMQ (`services/RabbitmqService`): envio para a fila configurada em `spring.rabbitmq.queue`.

### Estrutura de pastas (resumo)

```
src/main/java/io/github/beinlichsimone/vacationtrip/
  controller/        # REST Controllers
  services/          # Regras de negócio
  repository/        # Spring Data JPA
  model/             # Entidades JPA
  dto/               # DTOs de entrada/saída
  config/            # Segurança, CORS, validação
  client/            # OpenFeign
src/main/resources/  # properties, data.sql, logback
```

### Build e testes

```bash
mvn -DskipTests package   # build jar
mvn test                  # executar testes
```

### Troubleshooting

- 403 em produção: token ausente/expirado ou perfil errado; ver header `Authorization`.
- CORS no dev: confirme `allowedOrigins` em `WebConfig` (padrão `http://localhost:3000`).
- Swagger x Actuator: conflito conhecido (comentado nas properties). Desabilite Actuator ou ajuste Swagger.
- N+1 / Lazy/EAGER: revisar `fetch` e anotações `@Fetch(FetchMode.SUBSELECT)` em `Viagem`.

### Links úteis

- Guia para agentes (backend): `AGENTS.md`
- Guia para agentes (frontend): `frontend/AGENTS.md`
- Frontend (README): `frontend/README.md`
- Logs Papertrail (histórico do curso): https://my.papertrailapp.com/systems/nbbnu015491/events?focus=1508567656565989380