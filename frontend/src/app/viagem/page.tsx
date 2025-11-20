"use client";
import { listViagens, createViagem, updateViagem, deleteViagem, getViagem, ViagemDTO, uploadViagemImagem } from "@/lib/api";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { Stack, Card, CardContent, TextField, Button, Alert, Typography, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material";

export default function ViagemPage() {
  const searchParams = useSearchParams();
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState<ViagemDTO>({ nome: "", descricao: "", imagem: "" });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [uploading, setUploading] = useState(false);
  const [localPreview, setLocalPreview] = useState<string | null>(null);
  const [pendingFile, setPendingFile] = useState<File | null>(null);

  const load = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await listViagens();
      const items = res?.content ?? res ?? [];
      setData(items);
    } catch (e: any) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const editIdParam = searchParams.get("editId");
    if (editIdParam) {
      const id = Number(editIdParam);
      setLoading(true);
      setError(null);
      (async () => {
        try {
          const v = await getViagem(id);
          setData([v]);
          setEditingId(id);
          setForm({ nome: v.nome ?? "", descricao: v.descricao ?? "", dataIda: v.dataIda, dataVolta: v.dataVolta, imagem: (v as any).imagem });
        } catch (e: any) {
          setError(e.message);
        } finally {
          setLoading(false);
        }
      })();
    } else {
      load();
    }
  }, [searchParams]);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingId) {
        await updateViagem(editingId, form);
      } else {
        const payload = pendingFile ? { ...form, imagem: "" } : form;
        const created = await createViagem(payload);
        const createdId = (created as any)?.id;
        if (createdId && pendingFile) {
          setUploading(true);
          try {
            const updated = await uploadViagemImagem(createdId, pendingFile);
            setForm((prev) => ({ ...prev, imagem: updated.imagem }));
          } finally {
            setUploading(false);
          }
        }
      }
      setForm({ nome: "", descricao: "", imagem: "" });
      setPendingFile(null);
      setLocalPreview(null);
      setEditingId(null);
      await load();
    } catch (e: any) {
      alert(e.message);
    }
  };

  const onPickFile = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const f = e.target.files?.[0];
    if (!f) return;
    if (editingId) {
      const url = URL.createObjectURL(f);
      setLocalPreview(url);
      try {
        setUploading(true);
        const v = await uploadViagemImagem(editingId, f);
        setForm((prev) => ({ ...prev, imagem: v.imagem }));
        await load();
      } catch (err: any) {
        alert(err.message);
      } finally {
        setUploading(false);
        URL.revokeObjectURL(url);
        setLocalPreview(null);
        e.target.value = "";
      }
    } else {
      setPendingFile(f);
      const reader = new FileReader();
      reader.onload = () => {
        const dataUrl = reader.result as string;
        setLocalPreview(dataUrl);
        e.target.value = "";
      };
      reader.readAsDataURL(f);
    }
  };

  const onEdit = (v: any) => {
    setEditingId(v.id);
    setForm({ nome: v.nome ?? "", descricao: v.descricao ?? "", dataIda: v.dataIda, dataVolta: v.dataVolta, imagem: v.imagem });
  };

  const onDelete = async (id: number) => {
    if (!confirm("Excluir?")) return;
    await deleteViagem(id);
    await load();
  };

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Viagens</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} component="form" onSubmit={submit} direction="row" alignItems="flex-start">
            <Stack spacing={2} sx={{ maxWidth: 480, width: "100%" }}>
              <TextField label="Nome" value={form.nome ?? ""} onChange={(e) => setForm({ ...form, nome: e.target.value })} required fullWidth />
              <TextField label="Descrição" value={form.descricao ?? ""} onChange={(e) => setForm({ ...form, descricao: e.target.value })} fullWidth />
              <TextField
                label="Data de ida"
                type="date"
                value={form.dataIda ?? ""}
                onChange={(e) => setForm({ ...form, dataIda: e.target.value })}
                InputLabelProps={{ shrink: true }}
                fullWidth
              />
              <TextField
                label="Data de volta"
                type="date"
                value={form.dataVolta ?? ""}
                onChange={(e) => setForm({ ...form, dataVolta: e.target.value })}
                InputLabelProps={{ shrink: true }}
                fullWidth
              />
              <Button type="submit" variant="contained">{editingId ? "Salvar" : "Criar"}</Button>
            </Stack>
            <Stack spacing={1} sx={{ width: 240 }}>
              {(localPreview || form.imagem) && (
                <img src={(localPreview || (form.imagem as string))} alt="Preview da viagem" style={{ width: 240, height: 120, objectFit: "cover", borderRadius: 8 }} />
              )}
              <Button component="label" variant="outlined" size="small" disabled={uploading}>
                {uploading ? "Enviando..." : "Selecionar imagem"}
                <input hidden type="file" accept="image/*" onChange={onPickFile} />
              </Button>
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      {loading ? <Typography>Carregando...</Typography> : error ? <Alert severity="error">{error}</Alert> : (
        <Card>
          <CardContent>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Nome</TableCell>
                  <TableCell>Descrição</TableCell>
                  <TableCell>Data ida</TableCell>
                  <TableCell>Data volta</TableCell>
                  <TableCell>Ações</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.map((v) => (
                  <TableRow key={v.id}>
                    <TableCell>{v.id}</TableCell>
                    <TableCell>{v.nome}</TableCell>
                    <TableCell>{v.descricao}</TableCell>
                    <TableCell>{v.dataIda ? new Date(v.dataIda).toLocaleDateString() : ""}</TableCell>
                    <TableCell>{v.dataVolta ? new Date(v.dataVolta).toLocaleDateString() : ""}</TableCell>
                    <TableCell>
                      <Button size="small" onClick={() => onEdit(v)}>Editar</Button>
                      <Button size="small" color="error" onClick={() => onDelete(v.id)} sx={{ ml: 1 }}>Excluir</Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}
    </Stack>
  );
}


