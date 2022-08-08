package io.github.beinlichsimone.vacationtrip.dto.viagem;

import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;


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

    public Viagem atualizar (Integer id, ViagemRepository viagemRepository){
        Optional<Viagem> viagemOp = viagemRepository.findById(id);
        Viagem viagem = viagemOp.get();

        viagem.setNome(this.nome);
        viagem.setDescricao(this.descricao);

        return viagem;
    }
}
