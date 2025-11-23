export type HttpMethod = "GET" | "POST" | "PUT" | "DELETE";

const API_BASE = "/api"; // via next.config.js rewrites para o backend

export class ApiError extends Error {
  status: number;
  fieldErrors?: Record<string, string>;
  constructor(message: string, status: number, fieldErrors?: Record<string, string>) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.fieldErrors = fieldErrors;
  }
}

function getAuthHeader(): Record<string, string> {
  if (typeof window === "undefined") return {};
  const token = localStorage.getItem("auth_token");
  // Sempre envia o token quando existir; sem token, não envia Authorization
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export async function api<T>(path: string, options?: { method?: HttpMethod; body?: unknown; headers?: Record<string, string>; }): Promise<T> {
  const { method = "GET", body, headers = {} } = options || {};
  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers: {
      "Content-Type": "application/json",
      ...getAuthHeader(),
      ...headers,
    },
    body: body ? JSON.stringify(body) : undefined,
    cache: "no-store",
  });

  if (!res.ok) {
    const contentType = res.headers.get("content-type") || "";
    try {
      if (contentType.includes("application/json")) {
        const data = await res.json();
        // Caso 1: lista de erros [{ campo, erro }]
        if (Array.isArray(data) && data.length && data[0]?.campo && data[0]?.erro) {
          const fieldErrors: Record<string, string> = {};
          for (const it of data) {
            const k = String(it.campo);
            const v = String(it.erro);
            fieldErrors[k] = fieldErrors[k] ? `${fieldErrors[k]}; ${v}` : v;
          }
          const message = Object.entries(fieldErrors).map(([k, v]) => `${k}: ${v}`).join(" | ");
          throw new ApiError(message || "Requisição inválida", res.status, fieldErrors);
        }
        // Caso 2: objeto com message
        if (data?.message) {
          throw new ApiError(String(data.message), res.status);
        }
        // Fallback genérico
        throw new ApiError(JSON.stringify(data), res.status);
      } else {
        const text = await res.text();
        throw new ApiError(text || `Request failed: ${res.status}`, res.status);
      }
    } catch (e: any) {
      // Se deu erro ao parsear, garanta que lançamos algo útil
      if (e instanceof ApiError) throw e;
      const text = await res.text().catch(() => "");
      throw new ApiError(text || `Request failed: ${res.status}`, res.status);
    }
  }
  const contentType = res.headers.get("content-type");
  if (contentType && contentType.includes("application/json")) {
    return (await res.json()) as T;
  }
  return undefined as T;
}

export async function login(email: string, password: string): Promise<void> {
  try {
    const data = await api<{ token: string; tipo: string }>("/auth", {
      method: "POST",
      body: { email, password },
      headers: {},
    });
    if (typeof window !== "undefined") {
      localStorage.setItem("auth_token", data.token);
    }
  } catch (e: any) {
    const status = Number(e?.status ?? 0);
    const msg = String(e?.message ?? "");
    if (status === 400 || status === 401 || msg.includes("401") || msg.includes("400")) {
      throw new Error("Email ou senha inválidos.");
    }
    throw e;
  }
}

export async function register(nome: string, email: string, password: string): Promise<void> {
  // Backend retorna TokenDTO, então já salvamos o token
  try {
    const data = await api<{ token: string; tipo: string }>("/auth/register", {
      method: "POST",
      body: { nome, email, password },
    });
    if (typeof window !== "undefined" && data?.token) {
      localStorage.setItem("auth_token", data.token);
    }
  } catch (e: any) {
    const status = Number(e?.status ?? 0);
    const msg = String(e?.message ?? "");
    if (status === 409 || msg.includes("409")) {
      throw new Error("E-mail já cadastrado.");
    }
    throw e;
  }
}

// Viagem endpoints
export type ViagemDTO = {
  id?: number;
  nome: string;
  descricao?: string;
  dataIda?: string; // yyyy-MM-dd
  dataVolta?: string; // yyyy-MM-dd
  imagem?: string;
};

export async function listViagens(params?: { page?: number; size?: number }): Promise<any> {
  const query = new URLSearchParams();
  if (params?.page !== undefined) query.set("page", String(params.page));
  if (params?.size !== undefined) query.set("size", String(params.size));
  const suffix = query.toString() ? `?${query.toString()}` : "";
  return api(`/viagens${suffix}`);
}

export async function getViagem(id: number): Promise<ViagemDTO> {
  return api(`/viagens/${id}`);
}

export type ViagemDetalheDTO = ViagemDTO & {
  pessoas: PessoaDTO[];
  passeios: PasseioDTO[];
  deslocamentos: DeslocamentoDTO[];
  checklists: ChecklistDTO[];
};

export async function getViagemDetalhe(id: number): Promise<ViagemDetalheDTO> {
  return api(`/viagens/${id}/detalhe`);
}

export async function createViagem(v: ViagemDTO): Promise<ViagemDTO> {
  return api(`/viagens`, { method: "POST", body: v });
}

export async function updateViagem(id: number, v: ViagemDTO): Promise<ViagemDTO> {
  return api(`/viagens/${id}`, { method: "PUT", body: v });
}

export async function deleteViagem(id: number): Promise<void> {
  return api(`/viagens/${id}`, { method: "DELETE" });
}

export async function uploadViagemImagem(id: number, file: File): Promise<ViagemDTO> {
  const form = new FormData();
  form.append("file", file);
  const res = await fetch(`${API_BASE}/viagens/${id}/imagem`, {
    method: "POST",
    headers: {
      // não definir Content-Type manualmente; o browser define o boundary
      ...getAuthHeader(),
    },
    body: form,
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `Upload failed: ${res.status}`);
  }
  return (await res.json()) as ViagemDTO;
}

// Pessoa endpoints
export type PessoaDTO = { id?: number; nome: string; cpf?: string; email?: string; idViagem?: number };
export async function listPessoas(): Promise<any[]> {
  const res = await api<any>(`/pessoas`);
  return Array.isArray(res) ? res : (res?.content ?? []);
}
export async function getPessoa(id: number) { return api(`/pessoas/${id}`); }
export async function createPessoa(p: PessoaDTO) { return api(`/pessoas`, { method: "POST", body: p }); }
export async function updatePessoa(id: number, p: PessoaDTO) { return api(`/pessoas/${id}`, { method: "PUT", body: p }); }
export async function deletePessoa(id: number) { return api(`/pessoas/${id}`, { method: "DELETE" }); }

// Passeio endpoints
export type PasseioDTO = { id?: number; nome: string; descricao?: string; links?: string; dataPasseio?: string; idViagem: number };
export async function listPasseios(): Promise<any[]> {
  const res = await api<any>(`/passeios`);
  return Array.isArray(res) ? res : (res?.content ?? []);
}
export async function getPasseio(id: number) { return api(`/passeios/${id}`); }
export async function createPasseio(p: PasseioDTO) { return api(`/passeios`, { method: "POST", body: p }); }
export async function updatePasseio(id: number, p: PasseioDTO) { return api(`/passeios/${id}`, { method: "PUT", body: p }); }
export async function deletePasseio(id: number) { return api(`/passeios/${id}`, { method: "DELETE" }); }

// Documento endpoints
export type DocumentoDTO = { id?: number; nome: string; numero?: string; idPessoa: number };
export async function listDocumentos(): Promise<any[]> {
  const res = await api<any>(`/documentos`);
  return Array.isArray(res) ? res : (res?.content ?? []);
}
export async function getDocumento(id: number) { return api(`/documentos/${id}`); }
export async function createDocumento(d: DocumentoDTO) { return api(`/documentos`, { method: "POST", body: d }); }
export async function updateDocumento(id: number, d: DocumentoDTO) { return api(`/documentos/${id}`, { method: "PUT", body: d }); }
export async function deleteDocumento(id: number) { return api(`/documentos/${id}`, { method: "DELETE" }); }

// Deslocamento endpoints
export type DeslocamentoDTO = {
  id?: number;
  nome: string;
  local?: string;
  descricao?: string;
  valor?: number;
  link?: string;
  veiculo?: string;
  ida?: string; // ISO
  volta?: string; // ISO
  idPasseio: number;
  idViagem: number;
};
export async function listDeslocamentos(): Promise<any[]> {
  const res = await api<any>(`/deslocamentos`);
  return Array.isArray(res) ? res : (res?.content ?? []);
}
export async function getDeslocamento(id: number) { return api(`/deslocamentos/${id}`); }
export async function createDeslocamento(d: DeslocamentoDTO) { return api(`/deslocamentos`, { method: "POST", body: d }); }
export async function updateDeslocamento(id: number, d: DeslocamentoDTO) { return api(`/deslocamentos/${id}`, { method: "PUT", body: d }); }
export async function deleteDeslocamento(id: number) { return api(`/deslocamentos/${id}`, { method: "DELETE" }); }

// Checklist endpoints
export type ChecklistDTO = { id?: number; nome: string; checked?: boolean; observacao?: string; idViagem?: number };
export async function listChecklists(): Promise<any[]> {
  const res = await api<any>(`/checklists`);
  return Array.isArray(res) ? res : (res?.content ?? []);
}
export async function getChecklist(id: number) { return api(`/checklists/${id}`); }
export async function createChecklist(c: ChecklistDTO) { return api(`/checklists`, { method: "POST", body: c }); }
export async function updateChecklist(id: number, c: ChecklistDTO) { return api(`/checklists/${id}`, { method: "PUT", body: c }); }
export async function deleteChecklist(id: number) { return api(`/checklists/${id}`, { method: "DELETE" }); }


