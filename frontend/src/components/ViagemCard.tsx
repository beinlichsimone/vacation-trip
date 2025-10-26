"use client";
import * as React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import CardActionArea from "@mui/material/CardActionArea";

export type ViagemCardProps = {
  nome: string;
  descricao?: string;
  dataIda?: string;
  dataVolta?: string;
  onClick?: () => void;
  imageUrl?: string;
};

export default function ViagemCard(props: ViagemCardProps) {
  const { nome, descricao, onClick, imageUrl } = props;

  return (
    <Card sx={{ width: 320 }}>
      <CardActionArea onClick={onClick}>
        <CardMedia
          component="img"
          height="160"
          image={imageUrl || "/api/app-icon"}
          alt={nome}
          sx={{ objectFit: "cover" }}
        />
        <CardContent>
          <Typography gutterBottom variant="h6" component="div">
            {nome}
          </Typography>
          {descricao && (
            <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
              {descricao}
            </Typography>
          )}
        </CardContent>
      </CardActionArea>
    </Card>
  );
}


