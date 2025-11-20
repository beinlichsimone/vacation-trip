"use client";
import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { Alert, Breadcrumbs, Button, Card, CardContent, Chip, CircularProgress, Link as MLink, Stack, Table, TableBody, TableCell, TableHead, TableRow, Typography } from "@mui/material";
import { getViagemDetalhe, ViagemDetalheDTO } from "@/lib/api";

export default function ViagemDetalhePage() {
  const params = useParams();
  const router = useRouter();
  const [data, setData] = useState<ViagemDetalheDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const idParam = params?.id as string | undefined;
    if (!idParam) return;
    const id = Number(idParam);
    setLoading(true);
    setError(null);
    (async () => {
      try {
        const res = await getViagemDetalhe(id);
        setData(res);
      } catch (e: any) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    })();
  }, [params]);

  return (
    <Stack spacing={2}>
      <Breadcrumbs aria-label="breadcrumb">
        <MLink component="button" onClick={() => router.push("/")}>Viagens</MLink>
        <Typography color="text.primary">Detalhes</Typography>
      </Breadcrumbs>

      {loading ? (
        <Stack alignItems="center" py={4}><CircularProgress /></Stack>
      ) : error ? (
        <Alert severity="error">{error}</Alert>
      ) : data ? (
        <Stack spacing={2}>
          <Typography variant="h4">{data.nome}</Typography>
          { (data as any).imagem && (
            <img src={(data as any).imagem as string} alt={data.nome} style={{ width: 480, maxWidth: "100%", height: 240, objectFit: "cover", borderRadius: 8 }} />
          )}
          {data.descricao && <Typography variant="body1">{data.descricao}</Typography>}
          <Stack direction="row" spacing={1}>
            {data.dataIda && <Chip label={`Ida: ${new Date(data.dataIda as any).toLocaleDateString()}`} />}
            {data.dataVolta && <Chip label={`Volta: ${new Date(data.dataVolta as any).toLocaleDateString()}`} />}
          </Stack>

          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Pessoas</Typography>
              {data.pessoas?.length ? (
                <Table size="small">
                  <TableHead>
                    <TableRow>
                      <TableCell>Nome</TableCell>
                      <TableCell>CPF</TableCell>
                      <TableCell>Email</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {data.pessoas.map((p, idx) => (
                      <TableRow key={idx}>
                        <TableCell>{p.nome}</TableCell>
                        <TableCell>{p.cpf}</TableCell>
                        <TableCell>{p.email}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              ) : (
                <Typography variant="body2" color="text.secondary">Nenhuma pessoa vinculada.</Typography>
              )}
            </CardContent>
          </Card>

          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Passeios</Typography>
              {data.passeios?.length ? (
                <Table size="small">
                  <TableHead>
                    <TableRow>
                      <TableCell>Nome</TableCell>
                      <TableCell>Descrição</TableCell>
                      <TableCell>Data</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {data.passeios.map((p, idx) => (
                      <TableRow key={idx}>
                        <TableCell>{p.nome}</TableCell>
                        <TableCell>{p.descricao}</TableCell>
                        <TableCell>{p.dataPasseio ? new Date(p.dataPasseio as any).toLocaleDateString() : ""}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              ) : (
                <Typography variant="body2" color="text.secondary">Nenhum passeio cadastrado.</Typography>
              )}
            </CardContent>
          </Card>

          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Deslocamentos</Typography>
              {data.deslocamentos?.length ? (
                <Table size="small">
                  <TableHead>
                    <TableRow>
                      <TableCell>Nome</TableCell>
                      <TableCell>Local</TableCell>
                      <TableCell>Veículo</TableCell>
                      <TableCell>Ida</TableCell>
                      <TableCell>Volta</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {data.deslocamentos.map((d, idx) => (
                      <TableRow key={idx}>
                        <TableCell>{d.nome}</TableCell>
                        <TableCell>{d.local}</TableCell>
                        <TableCell>{d.veiculo}</TableCell>
                        <TableCell>{d.ida ? new Date(d.ida as any).toLocaleString() : ""}</TableCell>
                        <TableCell>{d.volta ? new Date(d.volta as any).toLocaleString() : ""}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              ) : (
                <Typography variant="body2" color="text.secondary">Nenhum deslocamento cadastrado.</Typography>
              )}
            </CardContent>
          </Card>
        </Stack>
      ) : null}
    </Stack>
  );
}



