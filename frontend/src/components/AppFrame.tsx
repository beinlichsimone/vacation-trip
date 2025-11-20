"use client";
import Providers from "@/app/providers";
import { AppBar, Toolbar, Container, Button, Link as MuiLink, Box } from "@mui/material";
import Link from "next/link";
import { PropsWithChildren } from "react";

export default function AppFrame({ children }: PropsWithChildren) {
  return (
    <Providers>
      <AppBar position="sticky" color="transparent" elevation={0} sx={{ borderBottom: 1, borderColor: "divider", backdropFilter: "blur(8px)", backgroundImage: "none" }}>
        <Toolbar sx={{ gap: 2 }}>
          <Link href="/" passHref legacyBehavior><MuiLink underline="none" color="text.primary">Home</MuiLink></Link>
          <Link href="/viagem" passHref legacyBehavior><MuiLink underline="none" color="text.primary">Viagens</MuiLink></Link>
          <Link href="/pessoa" passHref legacyBehavior><MuiLink underline="none" color="text.primary">Pessoas</MuiLink></Link>
          <Link href="/passeio" passHref legacyBehavior><MuiLink underline="none" color="text.primary">Passeios</MuiLink></Link>
          <Link href="/documento" passHref legacyBehavior><MuiLink underline="none" color="text.primary">Documentos</MuiLink></Link>
          <Link href="/deslocamento" passHref legacyBehavior><MuiLink underline="none" color="text.primary">Deslocamentos</MuiLink></Link>
          <Link href="/checklist" passHref legacyBehavior><MuiLink underline="none" color="text.primary">Checklists</MuiLink></Link>
          <Box sx={{ ml: "auto" }} />
          <Link href="/login" passHref legacyBehavior><Button variant="contained">Login</Button></Link>
        </Toolbar>
      </AppBar>
      <Container sx={{ py: 3 }}>{children}</Container>
    </Providers>
  );
}


