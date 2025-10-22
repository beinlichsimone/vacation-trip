import "./globals.css";
import type { Metadata } from "next";
import AppFrame from "@/components/AppFrame";

export const metadata: Metadata = {
  title: "Vacation Trip",
  description: "Frontend",
  icons: {
    icon: "/api/app-icon",
    shortcut: "/api/app-icon",
    apple: "/api/app-icon",
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="pt-br">
      <body>
        <AppFrame>{children}</AppFrame>
      </body>
    </html>
  );
}


