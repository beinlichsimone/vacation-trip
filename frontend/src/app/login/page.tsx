"use client";
import { useState } from "react";
import { login } from "@/lib/api";
import { useRouter } from "next/navigation";
import { Card, CardContent, Stack, TextField, Button, Alert, Typography } from "@mui/material";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await login(email, password);
      router.push("/");
    } catch (err: any) {
      setError(err?.message || "Falha no login");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Stack spacing={2} maxWidth={380}>
      <Typography variant="h4">Login</Typography>
      <Card>
        <CardContent>
          <Stack spacing={2} component="form" onSubmit={onSubmit}>
            <TextField label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} fullWidth required />
            <TextField label="Senha" type="password" value={password} onChange={(e) => setPassword(e.target.value)} fullWidth required />
            <Button type="submit" variant="contained" disabled={loading}>{loading ? "Entrando..." : "Entrar"}</Button>
            {error && <Alert severity="error">{error}</Alert>}
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  );
}


