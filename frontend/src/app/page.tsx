"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Stack, Typography, Alert, CircularProgress, Card, CardActionArea, CardContent, CardMedia } from "@mui/material";
import Grid from "@mui/material/Grid";
import ViagemCard from "@/components/ViagemCard";
import { listViagens } from "@/lib/api";
import AddIcon from "@mui/icons-material/Add";

export default function HomePage() {
  const router = useRouter();
  const [viagens, setViagens] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await listViagens({ size: 12 });
        const items = res?.content ?? res ?? [];
        setViagens(items);
      } catch (e: any) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  return (
    <Stack spacing={2}>
      <Typography variant="h4">Viagens</Typography>
      {loading ? (
        <Stack alignItems="center" py={4}><CircularProgress /></Stack>
      ) : error ? (
        <Alert severity="error">{error}</Alert>
      ) : (
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6} md={4} lg={3}>
            <Card sx={{ width: 320 }}>
              <CardActionArea onClick={() => router.push("/viagem")}>
                <CardMedia
                  component="div"
                  sx={{ height: 160, display: "flex", alignItems: "center", justifyContent: "center", bgcolor: "action.hover" }}
                >
                  <AddIcon fontSize="large" />
                </CardMedia>
                <CardContent>
                  <Typography gutterBottom variant="h6" component="div">
                    Nova viagem
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Criar um novo planejamento de viagem
                  </Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
          {viagens.map((v) => (
            <Grid key={v.id} item xs={12} sm={6} md={4} lg={3}>
              <ViagemCard
                nome={v.nome}
                descricao={v.descricao}
                dataIda={v.dataIda}
                dataVolta={v.dataVolta}
                onClick={() => router.push(`/viagem?editId=${v.id}`)}
              />
            </Grid>
          ))}
          {viagens.length === 0 && (
            <Typography variant="body1">Nenhuma viagem cadastrada.</Typography>
          )}
        </Grid>
      )}
    </Stack>
  );
}


