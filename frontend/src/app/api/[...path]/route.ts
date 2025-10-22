import { NextRequest, NextResponse } from "next/server";

const BACKEND_BASE = process.env.BACKEND_URL || "http://localhost:8083";

async function forward(req: NextRequest, path: string, method: string) {
  const url = `${BACKEND_BASE}/${path}`;
  const headers = new Headers();
  // Encaminha apenas cabeçalhos essenciais; não repassa Origin/Host
  const contentType = req.headers.get("content-type");
  if (contentType) headers.set("content-type", contentType);
  const auth = req.headers.get("authorization");
  if (auth) headers.set("authorization", auth);

  const body = method === "GET" || method === "HEAD" ? undefined : await req.text();

  const res = await fetch(url, {
    method,
    headers,
    body,
  });

  const resText = await res.text();
  return new NextResponse(resText, {
    status: res.status,
    headers: { "content-type": res.headers.get("content-type") || "text/plain" },
  });
}

export async function OPTIONS() {
  // Responde preflight localmente para o browser não falar direto com o backend
  return new NextResponse(null, {
    status: 204,
    headers: {
      "access-control-allow-origin": "*",
      "access-control-allow-methods": "GET,POST,PUT,DELETE,OPTIONS",
      "access-control-allow-headers": "Content-Type, Authorization",
      "access-control-max-age": "3600",
    },
  });
}

export async function GET(req: NextRequest, { params }: { params: { path: string[] } }) {
  return forward(req, params.path.join("/"), "GET");
}

export async function POST(req: NextRequest, { params }: { params: { path: string[] } }) {
  return forward(req, params.path.join("/"), "POST");
}

export async function PUT(req: NextRequest, { params }: { params: { path: string[] } }) {
  return forward(req, params.path.join("/"), "PUT");
}

export async function DELETE(req: NextRequest, { params }: { params: { path: string[] } }) {
  return forward(req, params.path.join("/"), "DELETE");
}


