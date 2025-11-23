"use client";
import { useState } from "react";
import { login, register, ApiError } from "@/lib/api";
import { useRouter } from "next/navigation";
import { Card, CardContent, Stack, TextField, Button, Alert, Typography, ToggleButtonGroup, ToggleButton } from "@mui/material";

export default function LoginPage() {
  const router = useRouter();
  const [mode, setMode] = useState<"login" | "register">("login");
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setFieldErrors({});
    try {
      if (mode === "login") {
        await login(email, password);
      } else {
        if (password !== confirm) {
          setError("As senhas não coincidem.");
          return;
        }
        await register(nome, email, password);
      }
      router.push("/");
    } catch (err: any) {
      if (err instanceof ApiError && err.fieldErrors) {
        setFieldErrors(err.fieldErrors);
        setError("Corrija os campos destacados.");
      } else {
        setError(err?.message || (mode === "login" ? "Falha no login" : "Falha no cadastro"));
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <Stack spacing={2} maxWidth={380}>
      <Typography variant="h4">{mode === "login" ? "Login" : "Criar conta"}</Typography>
      <ToggleButtonGroup
        color="primary"
        value={mode}
        exclusive
        onChange={(_, v) => v && setMode(v)}
        size="small"
      >
        <ToggleButton value="login">Entrar</ToggleButton>
        <ToggleButton value="register">Cadastrar</ToggleButton>
      </ToggleButtonGroup>
      <Card>
        <CardContent>
          <Stack spacing={2} component="form" onSubmit={onSubmit}>
            {mode === "register" && (
              <TextField
                label="Nome"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                fullWidth
                required
                error={!!fieldErrors.nome}
                helperText={fieldErrors.nome}
              />
            )}
            <TextField
              label="Email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              fullWidth
              required
              error={!!fieldErrors.email}
              helperText={fieldErrors.email}
            />
            <TextField
              label="Senha"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              fullWidth
              required
              error={!!fieldErrors.password}
              helperText={fieldErrors.password}
            />
            {mode === "register" && (
              <TextField
                label="Confirmar senha"
                type="password"
                value={confirm}
                onChange={(e) => setConfirm(e.target.value)}
                fullWidth
                required
              />
            )}
            <Button type="submit" variant="contained" disabled={loading}>
              {loading ? (mode === "login" ? "Entrando..." : "Cadastrando...") : (mode === "login" ? "Entrar" : "Cadastrar")}
            </Button>
            {error && (
              <Alert severity="error">
                {error}
              </Alert>
            )}
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  );
}


