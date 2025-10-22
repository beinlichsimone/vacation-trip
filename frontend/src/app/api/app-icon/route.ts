import { NextResponse } from "next/server";
import { readFile } from "fs/promises";

// Caminho absoluto da imagem fornecida no projeto
const ICON_ABS_PATH = "/Users/simone.beinlich.deal/Documents/Pessoais/git/vacation-trip-gp/vacation-trip-master/frontend/ChatGPT Image Oct 21, 2025, 05_57_50 PM.png";

export async function GET() {
  try {
    const data = await readFile(ICON_ABS_PATH);
    return new NextResponse(data, {
      status: 200,
      headers: {
        "content-type": "image/png",
        "cache-control": "public, max-age=31536000, immutable",
      },
    });
  } catch (e) {
    return new NextResponse("Icon not found", { status: 404 });
  }
}


