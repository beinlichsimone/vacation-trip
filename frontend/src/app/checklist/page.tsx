"use client";
import { useEffect, useState } from "react";
import { Stack, Card, CardContent, TextField, Select, MenuItem, Button, Typography, Table, TableHead, TableRow, TableCell, TableBody, Checkbox, FormControlLabel } from "@mui/material";
import { listChecklists, createChecklist, updateChecklist, deleteChecklist, listViagens, ChecklistDTO } from "@/lib/api";

export default function ChecklistPage() {
  const [data, setData] = useState<any[]>([]);
  const [viagens, setViagens] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState<ChecklistDTO>({ nome: "", checked: false, observacao: "", idViagem: 0 as unknown as number });
  const [editingId, setEditingId] = useState<number | null>(null);

  const load = async () => {
    setLoading(true);
    const [cRes, vRes] = await Promise.all([listChecklists(), listViagens()]);
    setData(Array.isArray(cRes) ? cRes : []);
    setViagens(Array.isArray(vRes?.content) ? vRes.content : Array.isArray(vRes) ? vRes : []);
    setLoading(false);
  };
  useEffect(() => { load(); }, []);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.nome || form.nome.trim() === "") return alert("Informe o nome");
    if (!form.idViagem) return alert("Selecione a viagem");
    if (editingId) await updateChecklist(editingId, form);
    else await createChecklist(form);
    setForm({ nome: "", checked: false, observacao: "", idViagem: 0 as unknown as number });
    setEditingId(null);
    await load();
  };

  const onEdit = (c: any) => {
    setEditingId(c.id);
    setForm({ nome: c.nome ?? "", checked: !!c.checked, observacao: c.observacao ?? "", idViagem: c.idViagem });
  };
  const onDelete = async (id: number) => { if (confirm("Excluir?")) { await deleteChecklist(id); await load(); } };

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Checklists</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} component="form" onSubmit={submit}>
            <Stack direction={{ xs: "column", md: "row" }} spacing={2} alignItems="stretch">
              <Stack spacing={2} sx={{ flex: 1 }}>
                <TextField label="Nome" value={form.nome ?? ""} onChange={(e) => setForm({ ...form, nome: e.target.value })} required fullWidth />
                <TextField label="Observação" value={form.observacao ?? ""} onChange={(e) => setForm({ ...form, observacao: e.target.value })} fullWidth />
              </Stack>
              <Stack spacing={2} sx={{ flex: 1 }}>
                <Select value={form.idViagem as unknown as number} onChange={(e) => setForm({ ...form, idViagem: Number(e.target.value) })} displayEmpty fullWidth>
                  <MenuItem value={0}>Selecione a viagem</MenuItem>
                  {viagens.map((v: any) => (<MenuItem key={v.id} value={v.id}>{v.nome}</MenuItem>))}
                </Select>
                <FormControlLabel
                  control={<Checkbox checked={!!form.checked} onChange={(e) => setForm({ ...form, checked: e.target.checked })} />}
                  label="Concluído"
                />
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
                  <TableCell>Concluído</TableCell>
                  <TableCell>Observação</TableCell>
                  <TableCell>Viagem</TableCell>
                  <TableCell>Ações</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.map((c) => (
                  <TableRow key={c.id}>
                    <TableCell>{c.id}</TableCell>
                    <TableCell>{c.nome}</TableCell>
                    <TableCell>{c.checked ? "Sim" : "Não"}</TableCell>
                    <TableCell>{c.observacao}</TableCell>
                    <TableCell>{c.idViagem}</TableCell>
                    <TableCell>
                      <Button size="small" onClick={() => onEdit(c)}>Editar</Button>
                      <Button size="small" color="error" onClick={() => onDelete(c.id)} sx={{ ml: 1 }}>Excluir</Button>
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

