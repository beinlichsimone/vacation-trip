Crie um arquivo `.env.local` na pasta `frontend` com:

NEXT_PUBLIC_REQUIRE_AUTH=false
BACKEND_URL=http://localhost:8083

Quando quiser exigir token JWT no frontend, mude para:

NEXT_PUBLIC_REQUIRE_AUTH=true

