# AGENTS.md — Frontend (Next.js)

Guia rápido para agentes (humanos e IA) trabalharem com o frontend do projeto Vacation Trip.

## Visão e stack

- Next.js 14 (App Router), React 18, TypeScript
- UI: MUI v7 + Emotion; `@mui/material-nextjs` para SSR/Cache
- Comunicação: `fetch` via cliente central `src/lib/api.ts` com base `"/api"`

## Como executar

Pré-requisitos: Node.js 18+ e npm.

1. Instale dependências:
   ```bash
   npm install
   ```
2. Configure `.env.local`:
   ```ini
   BACKEND_URL=http://localhost:8083
   NEXT_PUBLIC_REQUIRE_AUTH=false
   ```
3. Rode em desenvolvimento:
   ```bash
   npm run dev
   ```
4. Acesse `http://localhost:3000`.

Build/produção:
```bash
npm run build
npm start
```

## Variáveis de ambiente

- `BACKEND_URL`: URL do backend Spring Boot
- `NEXT_PUBLIC_REQUIRE_AUTH`: quando `true`, o cliente envia `Authorization: Bearer <token>` do `localStorage`

## Arquitetura

- Rotas: `src/app/*` (ex.: `viagem/page.tsx`, `pessoa/page.tsx`, `passeio/page.tsx`)
- Layout e Providers:
  - `src/components/AppFrame.tsx`: AppBar e navegação
  - `src/app/providers.tsx`: ThemeProvider, CssBaseline, AppRouterCacheProvider
  - `src/app/layout.tsx`: HTML raiz + `AppFrame`
- Cliente HTTP: `src/lib/api.ts`
  - Base `"/api"`; injeta `Authorization` quando `NEXT_PUBLIC_REQUIRE_AUTH=true`
- Proxy/Rewrite:
  - `next.config.js`: rewrite `/api/:path* -> BACKEND_URL/:path*`
  - `src/app/api/[...path]/route.ts`: proxy server-side (encaminha método, body e cabeçalhos essenciais)
- Autenticação:
  - `src/app/login/page.tsx` usa `login()` → `POST /auth` no backend (perfil prod)
  - Token é salvo em `localStorage` como `auth_token`

## Playbooks (tarefas comuns)

1) Criar CRUD para novo recurso
- Adicione funções no `src/lib/api.ts` (list/get/create/update/delete)
- Crie página em `src/app/<recurso>/page.tsx` (hooks, formulários MUI, tabela)
- Adicione link no `src/components/AppFrame.tsx`
- Teste fluxos: listar, criar, editar, excluir

2) Habilitar autenticação no cliente
- Ajuste `.env.local`: `NEXT_PUBLIC_REQUIRE_AUTH=true`
- Garanta login funcional em `/login` e backend em perfil `prod`

3) Tratar erros de rede e feedback
- Capture `res.ok` falso no `lib/api.ts` (já implementado) e exiba `Alert` nas páginas

4) Ajustar CORS ou proxy
- Prefira usar o rewrite (`next.config.js`)
- Se necessário, use a rota de proxy `app/api/[...path]` para repassar `Authorization`

## Depuração e problemas comuns

- 403 em dev: normalmente `NEXT_PUBLIC_REQUIRE_AUTH=true` com backend em `dev` (sem `/auth`); defina `false`
- CORS: confirme rewrite ativo e `BACKEND_URL`; evite chamar o backend diretamente do browser
- Token ausente: ver `localStorage.auth_token` e headers no DevTools

## Referências rápidas de caminhos

```
src/
  app/
    viagem/page.tsx
    pessoa/page.tsx
    passeio/page.tsx
    documento/page.tsx
    deslocamento/page.tsx
    login/page.tsx
    api/[...path]/route.ts
    layout.tsx
    providers.tsx
    globals.css
  components/AppFrame.tsx
  lib/api.ts
  theme.ts
```


