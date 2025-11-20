"use client";
import { listPessoas, createPessoa, updatePessoa, deletePessoa, PessoaDTO, listViagens } from "@/lib/api";
import { useEffect, useState } from "react";
import { Stack, Card, CardContent, TextField, Button, Typography, Table, TableHead, TableRow, TableCell, TableBody, Select, MenuItem } from "@mui/material";

export default function PessoaPage() {
  const [data, setData] = useState<any[]>([]);
  const [viagens, setViagens] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState<PessoaDTO>({ nome: "" });
  const [editingId, setEditingId] = useState<number | null>(null);

  const load = async () => {
    setLoading(true);
    try {
      const [dRes, vRes] = await Promise.all([listPessoas(), listViagens()]);
      setData(Array.isArray(dRes) ? dRes : []);
      setViagens(Array.isArray(vRes) ? vRes : (vRes as any)?.content ?? []);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { load(); }, []);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (editingId) await updatePessoa(editingId, form);
    else await createPessoa(form);
    setForm({ nome: "" });
    setEditingId(null);
    await load();
  };

  const onEdit = (p: any) => { setEditingId(p.id); setForm({ nome: p.nome ?? "", cpf: p.cpf ?? "", email: p.email ?? "", idViagem: p.idViagem }); };
  const onDelete = async (id: number) => { if (confirm("Excluir?")) { await deletePessoa(id); await load(); } };

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Pessoas</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} component="form" onSubmit={submit}>
            <Stack direction={{ xs: "column", md: "row" }} spacing={2} alignItems="stretch">
              <Stack spacing={2} sx={{ flex: 1 }}>
                <TextField label="Nome" value={form.nome ?? ""} onChange={(e) => setForm({ ...form, nome: e.target.value })} required fullWidth />
                <TextField label="Email" value={form.email ?? ""} onChange={(e) => setForm({ ...form, email: e.target.value })} fullWidth />
              </Stack>
              <Stack spacing={2} sx={{ flex: 1 }}>
                <TextField label="CPF" value={form.cpf ?? ""} onChange={(e) => setForm({ ...form, cpf: e.target.value })} fullWidth />
                <Select value={(form.idViagem ?? 0) as number} onChange={(e) => setForm({ ...form, idViagem: Number(e.target.value) })} displayEmpty fullWidth>
                  <MenuItem value={0}>Selecione a viagem</MenuItem>
                  {viagens.map((p: any) => (<MenuItem key={p.id} value={p.id}>{p.nome}</MenuItem>))}
                </Select>
              </Stack>
            </Stack>
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
                  <TableCell>CPF</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Viagem</TableCell>
                  <TableCell>Ações</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.map((p) => (
                  <TableRow key={p.id}>
                    <TableCell>{p.id}</TableCell>
                    <TableCell>{p.nome}</TableCell>
                    <TableCell>{p.cpf}</TableCell>
                    <TableCell>{p.email}</TableCell>
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


