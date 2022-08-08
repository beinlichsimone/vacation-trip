package io.github.beinlichsimone.vacationtrip.dto.viagem;

import io.github.beinlichsimone.vacationtrip.model.Viagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViagemCadastroDTO {
    private String nome;
    private String descricao;
    private LocalDate dataIda;
    private LocalDate dataVolta;

    public ViagemCadastroDTO(Viagem viagem) {
        this.nome = viagem.getNome();
        this.descricao = viagem.getDescricao();
        this.dataIda = viagem.getDataIda();
        this.dataVolta = viagem.getDataVolta();
    }

    public Viagem converter() {
        return new Viagem(nome, descricao, dataIda, dataVolta);
    }
}
