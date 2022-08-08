package io.github.beinlichsimone.vacationtrip.dto.viagem;

import io.github.beinlichsimone.vacationtrip.model.Viagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViagemRetornoDTO {
    private Integer Id;
    private String nome;
    private String descricao;
    private LocalDate dataIda;
    private LocalDate dataVolta;

    public ViagemRetornoDTO(Viagem viagem) {
        this.Id = viagem.getId();
        this.nome = viagem.getNome();
        this.descricao = viagem.getDescricao();
        this.dataIda = viagem.getDataIda();
        this.dataVolta = viagem.getDataVolta();
    }

    public static Page<ViagemRetornoDTO> converterParaDTO (Page<Viagem> viagens) {
        return viagens.map(ViagemRetornoDTO::new);
    }
}
