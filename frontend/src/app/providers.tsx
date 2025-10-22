"use client";
import { AppRouterCacheProvider } from "@mui/material-nextjs/v14-appRouter";
import { ThemeProvider, CssBaseline } from "@mui/material";
import theme from "@/theme";
import { PropsWithChildren } from "react";

export default function Providers({ children }: PropsWithChildren) {
  return (
    <AppRouterCacheProvider>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        {children}
      </ThemeProvider>
    </AppRouterCacheProvider>
  );
}


