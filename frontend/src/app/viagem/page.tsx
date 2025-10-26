"use client";
import { listViagens, createViagem, updateViagem, deleteViagem, getViagem, ViagemDTO } from "@/lib/api";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { Stack, Card, CardContent, TextField, Button, Alert, Typography, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material";

export default function ViagemPage() {
  const searchParams = useSearchParams();
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState<ViagemDTO>({ nome: "", descricao: "" });
  const [editingId, setEditingId] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);

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
          setForm({ nome: v.nome ?? "", descricao: v.descricao ?? "", dataIda: v.dataIda, dataVolta: v.dataVolta });
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
      if (editingId) await updateViagem(editingId, form);
      else await createViagem(form);
      setForm({ nome: "", descricao: "" });
      setEditingId(null);
      await load();
    } catch (e: any) {
      alert(e.message);
    }
  };

  const onEdit = (v: any) => {
    setEditingId(v.id);
    setForm({ nome: v.nome ?? "", descricao: v.descricao ?? "", dataIda: v.dataIda, dataVolta: v.dataVolta });
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
          <Stack spacing={2} maxWidth={480} component="form" onSubmit={submit}>
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


