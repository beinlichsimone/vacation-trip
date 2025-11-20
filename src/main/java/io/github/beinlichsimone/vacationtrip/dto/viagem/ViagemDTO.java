package io.github.beinlichsimone.vacationtrip.dto.viagem;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViagemDTO {

    private Integer Id;
    private String nome;
    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataIda;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataVolta;

    private String imagem;

    public ViagemDTO(Viagem viagem) {
        this.Id = viagem.getId();
        this.nome = viagem.getNome();
        this.descricao = viagem.getDescricao();
        this.dataIda = viagem.getDataIda();
        this.dataVolta = viagem.getDataVolta();
        this.imagem = viagem.getImagem();
    }

    public static Page<ViagemDTO> converterParaDTOcomPage(Page<Viagem> viagens) {
        return viagens.map(ViagemDTO::new);
    }
}
