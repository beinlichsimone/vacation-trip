package io.github.beinlichsimone.vacationtrip.dto.viagem;

import io.github.beinlichsimone.vacationtrip.model.Viagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViagemAtualizaDTO {
    private String nome;
    private String descricao;

    public ViagemAtualizaDTO(Viagem viagem) {
        this.nome = viagem.getNome();
        this.descricao = viagem.getDescricao();
    }

}
