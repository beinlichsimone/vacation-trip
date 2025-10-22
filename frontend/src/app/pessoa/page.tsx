"use client";
import { listPessoas, createPessoa, updatePessoa, deletePessoa, PessoaDTO } from "@/lib/api";
import { useEffect, useState } from "react";
import { Stack, Card, CardContent, TextField, Button, Typography, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material";

export default function PessoaPage() {
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState<PessoaDTO>({ nome: "" });
  const [editingId, setEditingId] = useState<number | null>(null);

  const load = async () => {
    setLoading(true);
    try {
      const res = await listPessoas();
      setData(res ?? []);
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

  const onEdit = (p: any) => { setEditingId(p.id); setForm({ nome: p.nome ?? "", cpf: p.cpf ?? "", email: p.email ?? "" }); };
  const onDelete = async (id: number) => { if (confirm("Excluir?")) { await deletePessoa(id); await load(); } };

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Pessoas</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} maxWidth={480} component="form" onSubmit={submit}>
            <TextField label="Nome" value={form.nome ?? ""} onChange={(e) => setForm({ ...form, nome: e.target.value })} required fullWidth />
            <TextField label="CPF" value={form.cpf ?? ""} onChange={(e) => setForm({ ...form, cpf: e.target.value })} fullWidth />
            <TextField label="Email" value={form.email ?? ""} onChange={(e) => setForm({ ...form, email: e.target.value })} fullWidth />
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


