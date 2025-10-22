## Vacation Trip — Frontend

Aplicação web em Next.js 14 (React 18 + TypeScript) com MUI v7 para gerenciar viagens, pessoas, documentos, deslocamentos e passeios. Comunica-se com o backend Spring Boot via proxy/rewrite em `/api`.

### Arquitetura

- **App Router (Next.js 14)**: rotas em `src/app/*` (ex.: `viagem/page.tsx`, `pessoa/page.tsx`, etc.). Páginas são majoritariamente Client Components para interatividade (CRUD).
- **Layout e Providers**
  - `src/components/AppFrame.tsx`: AppBar + navegação e container da página.
  - `src/app/providers.tsx`: integrações do MUI (ThemeProvider, CssBaseline) e `@mui/material-nextjs` para caching/SSR (`AppRouterCacheProvider`).
  - `src/app/layout.tsx`: estrutura HTML raiz e injeção do `AppFrame`.
- **Camada de API (cliente HTTP)**
  - `src/lib/api.ts`: funções de acesso REST (CRUD de `viagem`, `pessoa`, `passeio`, `documento`, `deslocamento`) com `fetch`.
  - Base de chamadas: `"/api"`; cabeçalho `Authorization: Bearer <token>` é incluído quando `NEXT_PUBLIC_REQUIRE_AUTH=true`.
- **Proxy/Rewrite para o backend**
  - `next.config.js` define rewrite: `/api/:path* -> BACKEND_URL/:path*` (padrão `http://localhost:8083`).
  - `src/app/api/[...path]/route.ts`: rota de API Next que também faz proxy (encaminha método, body e cabeçalhos essenciais). Útil para uniformizar CORS e forwarding do `Authorization`.
- **Autenticação**
  - `src/app/login/page.tsx` usa `lib/api.login()` que chama `POST /auth` no backend (em perfil prod).
  - Token JWT é salvo em `localStorage` como `auth_token` e enviado nas requisições quando `NEXT_PUBLIC_REQUIRE_AUTH=true`.
- **Estilização**
  - MUI v7 + Emotion. Tema em `src/theme.ts`. Reset global em `src/app/globals.css`.

### Requisitos

- Node.js 18+ (LTS recomendado) e npm.
- Backend executando em `http://localhost:8083` ou defina `BACKEND_URL`.

### Configuração

Crie `frontend/.env.local` (exemplo):

```ini
# URL do backend Spring Boot
BACKEND_URL=http://localhost:8083

# Quando true, o cliente envia Authorization: Bearer <token> do localStorage
NEXT_PUBLIC_REQUIRE_AUTH=false
```

- Em desenvolvimento, normalmente `NEXT_PUBLIC_REQUIRE_AUTH=false`.
- Em produção, habilite `NEXT_PUBLIC_REQUIRE_AUTH=true` e garanta que o backend esteja com perfil `prod`.

### Scripts

- `npm run dev`: inicia o servidor de desenvolvimento em `http://localhost:3000`.
- `npm run build`: build de produção.
- `npm start`: inicia o build de produção.
- `npm run lint`: validação ESLint.

### Como rodar

1. Instalar dependências:
   ```bash
   npm install
   ```
2. Configurar `.env.local` (veja seção Configuração).
3. Iniciar:
   ```bash
   npm run dev
   ```
4. Acesse `http://localhost:3000`.

### Comunicação com o backend

- Todas as chamadas do frontend usam `"/api"` como base.
- Em runtime:
  - O rewrite (`next.config.js`) mapeia `/api/:path*` para `BACKEND_URL/:path*`.
  - Alternativamente/ adicionalmente, a rota `src/app/api/[...path]/route.ts` atua como proxy, repassando `GET/POST/PUT/DELETE`, body e `Authorization`.

### Fluxo de autenticação

- Faça login em `/login` (em produção, quando o backend exige JWT).
- O token retornado por `POST /auth` é salvo em `localStorage` com chave `auth_token`.
- Com `NEXT_PUBLIC_REQUIRE_AUTH=true`, `src/lib/api.ts` envia `Authorization` automaticamente.

### Estrutura de pastas (resumo)

```
frontend/
  src/
    app/
      api/[...path]/route.ts   # Proxy de API
      viagem/page.tsx          # CRUD de viagens
      pessoa/page.tsx          # CRUD de pessoas
      passeio/page.tsx         # CRUD de passeios
      documento/page.tsx       # CRUD de documentos
      deslocamento/page.tsx    # CRUD de deslocamentos
      login/page.tsx           # Login
      layout.tsx               # HTML root + AppFrame
      globals.css
      providers.tsx            # Providers MUI/App Router
    components/
      AppFrame.tsx             # AppBar e navegação
    lib/
      api.ts                   # Cliente HTTP central
    theme.ts                   # Tema MUI
  next.config.js               # Rewrites /api -> BACKEND_URL
  package.json                 # Scripts e dependências
```

### Convenções rápidas

- TypeScript por padrão.
- Preferir usar funções de `src/lib/api.ts` para chamadas HTTP.
- Tratar erros de rede exibindo mensagens amigáveis (ex.: `Alert` do MUI).

### Build e deploy

- Build:
  ```bash
  npm run build
  npm start
  ```
- Variáveis de ambiente necessárias no servidor:
  - `BACKEND_URL` (ex.: `https://api.suaempresa.com`).
  - `NEXT_PUBLIC_REQUIRE_AUTH=true` (se o backend exigir JWT).


