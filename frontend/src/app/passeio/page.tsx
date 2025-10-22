"use client";
import { listPasseios, createPasseio, updatePasseio, deletePasseio, listViagens, PasseioDTO } from "@/lib/api";
import { useEffect, useState } from "react";
import { Stack, Card, CardContent, TextField, Select, MenuItem, Button, Typography, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material";

export default function PasseioPage() {
  const [data, setData] = useState<any[]>([]);
  const [viagens, setViagens] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState<PasseioDTO>({ nome: "", idViagem: 0 });
  const [editingId, setEditingId] = useState<number | null>(null);

  const load = async () => {
    setLoading(true);
    const [pRes, vRes] = await Promise.all([listPasseios(), listViagens()]);
    setData(Array.isArray(pRes) ? pRes : []);
    setViagens(Array.isArray(vRes?.content) ? vRes.content : Array.isArray(vRes) ? vRes : []);
    setLoading(false);
  };
  useEffect(() => { load(); }, []);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.idViagem) return alert("Selecione a viagem");
    if (editingId) await updatePasseio(editingId, form);
    else await createPasseio(form);
    setForm({ nome: "", idViagem: 0 });
    setEditingId(null);
    await load();
  };

  const onEdit = (p: any) => { setEditingId(p.id); setForm({ nome: p.nome ?? "", descricao: p.descricao ?? "", links: p.links ?? "", idViagem: p.idViagem }); };
  const onDelete = async (id: number) => { if (confirm("Excluir?")) { await deletePasseio(id); await load(); } };

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Passeios</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} maxWidth={480} component="form" onSubmit={submit}>
            <TextField label="Nome" value={form.nome ?? ""} onChange={(e) => setForm({ ...form, nome: e.target.value })} required fullWidth />
            <TextField label="Descrição" value={form.descricao ?? ""} onChange={(e) => setForm({ ...form, descricao: e.target.value })} fullWidth />
            <TextField label="Links" value={form.links ?? ""} onChange={(e) => setForm({ ...form, links: e.target.value })} fullWidth />
            <Select value={form.idViagem} onChange={(e) => setForm({ ...form, idViagem: Number(e.target.value) })} displayEmpty>
              <MenuItem value={0}>Selecione a viagem</MenuItem>
              {viagens.map((v: any) => (<MenuItem key={v.id} value={v.id}>{v.nome}</MenuItem>))}
            </Select>
            <Button type="submit" variant="contained">{editingId ? "Salvar" : "Criar"}</Button>
          </Stack>
        </CardContent>
      </Card>

      {loading ? <Typography>Carregando...</Typography> : (
        <Card>
          <CardContent>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Nome</TableCell>
                  <TableCell>Viagem</TableCell>
                  <TableCell>Ações</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.map((p) => (
                  <TableRow key={p.id}>
                    <TableCell>{p.id}</TableCell>
                    <TableCell>{p.nome}</TableCell>
                    <TableCell>{p.idViagem}</TableCell>
                    <TableCell>
                      <Button size="small" onClick={() => onEdit(p)}>Editar</Button>
                      <Button size="small" color="error" onClick={() => onDelete(p.id)} sx={{ ml: 1 }}>Excluir</Button>
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


