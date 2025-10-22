"use client";
import { Stack, Typography, Card, CardContent } from "@mui/material";

export default function HomePage() {
  return (
    <Stack spacing={2}>
      <Typography variant="h4">Vacation Trip</Typography>
      <Card>
        <CardContent>
          <Typography>Bem-vindo! Use o menu para navegar.</Typography>
        </CardContent>
      </Card>
    </Stack>
  );
}


