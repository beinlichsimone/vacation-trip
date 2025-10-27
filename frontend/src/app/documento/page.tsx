"use client";
import { listDocumentos, createDocumento, updateDocumento, deleteDocumento, listPessoas, DocumentoDTO } from "@/lib/api";
import { useEffect, useState } from "react";
import { Stack, Card, CardContent, TextField, Select, MenuItem, Button, Typography, Table, TableHead, TableRow, TableCell, TableBody } from "@mui/material";

export default function DocumentoPage() {
  const [data, setData] = useState<any[]>([]);
  const [pessoas, setPessoas] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState<DocumentoDTO>({ nome: "", idPessoa: 0 as unknown as number });
  const [editingId, setEditingId] = useState<number | null>(null);

  const load = async () => {
    setLoading(true);
    try {
      const [dRes, pRes] = await Promise.all([listDocumentos(), listPessoas()]);
      setData(Array.isArray(dRes) ? dRes : []);
      setPessoas(Array.isArray(pRes) ? pRes : []);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { load(); }, []);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.idPessoa) return alert("Selecione a pessoa");
    if (editingId) await updateDocumento(editingId, form);
    else await createDocumento(form);
    setForm({ nome: "", idPessoa: 0 as unknown as number });
    setEditingId(null);
    await load();
  };

  const onEdit = (d: any) => { setEditingId(d.id); setForm({ nome: d.nome ?? "", numero: d.numero ?? "", idPessoa: d.idPessoa }); };
  const onDelete = async (id: number) => { if (confirm("Excluir?")) { await deleteDocumento(id); await load(); } };

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Documentos</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} maxWidth={480} component="form" onSubmit={submit}>
            <TextField label="Nome" value={form.nome ?? ""} onChange={(e) => setForm({ ...form, nome: e.target.value })} required fullWidth />
            <TextField label="Número" value={form.numero ?? ""} onChange={(e) => setForm({ ...form, numero: e.target.value })} fullWidth />
            <Select value={form.idPessoa as unknown as number} onChange={(e) => setForm({ ...form, idPessoa: Number(e.target.value) })} displayEmpty>
              <MenuItem value={0}>Selecione a pessoa</MenuItem>
              {pessoas.map((p: any) => (<MenuItem key={p.id} value={p.id}>{p.nome}</MenuItem>))}
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
                  <TableCell>Número</TableCell>
                  <TableCell>Pessoa</TableCell>
                  <TableCell>Ações</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data.map((d) => (
                  <TableRow key={d.id}>
                    <TableCell>{d.id}</TableCell>
                    <TableCell>{d.nome}</TableCell>
                    <TableCell>{d.numero}</TableCell>
                    <TableCell>{d.idPessoa}</TableCell>
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


