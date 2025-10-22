"use client";
import { listDeslocamentos, createDeslocamento, updateDeslocamento, deleteDeslocamento, listViagens, listPasseios, DeslocamentoDTO } from "@/lib/api";
import { useEffect, useState } from "react";
import { Stack, Card, CardContent, TextField, Select, MenuItem, Button, Alert, Typography, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material";

export default function DeslocamentoPage() {
  const [data, setData] = useState<any[]>([]);
  const [viagens, setViagens] = useState<any[]>([]);
  const [passeios, setPasseios] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState<DeslocamentoDTO>({ nome: "", idViagem: 0 as unknown as number, idPasseio: 0 as unknown as number });
  const [editingId, setEditingId] = useState<number | null>(null);

  const load = async () => {
    setLoading(true);
    const [dRes, vRes, pRes] = await Promise.all([listDeslocamentos(), listViagens(), listPasseios()]);
    setData(Array.isArray(dRes) ? dRes : []);
    setViagens(Array.isArray(vRes?.content) ? vRes.content : Array.isArray(vRes) ? vRes : []);
    setPasseios(Array.isArray(pRes) ? pRes : []);
    setLoading(false);
  };
  useEffect(() => { load(); }, []);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.idViagem || !form.idPasseio) return alert("Selecione viagem e passeio");
    if (editingId) await updateDeslocamento(editingId, form);
    else await createDeslocamento(form);
    setForm({ nome: "", idViagem: 0 as unknown as number, idPasseio: 0 as unknown as number });
    setEditingId(null);
    await load();
  };

  const onEdit = (d: any) => { setEditingId(d.id); setForm({ nome: d.nome ?? "", local: d.local ?? "", descricao: d.descricao ?? "", idViagem: d.idViagem, idPasseio: d.idPasseio }); };
  const onDelete = async (id: number) => { if (confirm("Excluir?")) { await deleteDeslocamento(id); await load(); } };

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Deslocamentos</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} maxWidth={600} component="form" onSubmit={submit}>
            <TextField placeholder="Nome" label="Nome" value={form.nome ?? ""} onChange={(e) => setForm({ ...form, nome: e.target.value })} required fullWidth />
            <TextField placeholder="Local" label="Local" value={form.local ?? ""} onChange={(e) => setForm({ ...form, local: e.target.value })} fullWidth />
            <TextField placeholder="Descrição" label="Descrição" value={form.descricao ?? ""} onChange={(e) => setForm({ ...form, descricao: e.target.value })} fullWidth />
            <Select value={form.idViagem as unknown as number} onChange={(e) => setForm({ ...form, idViagem: Number(e.target.value) })} displayEmpty>
              <MenuItem value={0}>Selecione a viagem</MenuItem>
              {viagens.map((v: any) => (<MenuItem key={v.id} value={v.id}>{v.nome}</MenuItem>))}
            </Select>
            <Select value={form.idPasseio as unknown as number} onChange={(e) => setForm({ ...form, idPasseio: Number(e.target.value) })} displayEmpty>
              <MenuItem value={0}>Selecione o passeio</MenuItem>
              {passeios.map((p: any) => (<MenuItem key={p.id} value={p.id}>{p.nome}</MenuItem>))}
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
                  <TableCell>Passeio</TableCell>
                  <TableCell>Ações</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.map((d) => (
                  <TableRow key={d.id}>
                    <TableCell>{d.id}</TableCell>
                    <TableCell>{d.nome}</TableCell>
                    <TableCell>{d.idViagem}</TableCell>
                    <TableCell>{d.idPasseio}</TableCell>
                    <TableCell>
                      <Button size="small" onClick={() => onEdit(d)}>Editar</Button>
                      <Button size="small" color="error" onClick={() => onDelete(d.id)} sx={{ ml: 1 }}>Excluir</Button>
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


