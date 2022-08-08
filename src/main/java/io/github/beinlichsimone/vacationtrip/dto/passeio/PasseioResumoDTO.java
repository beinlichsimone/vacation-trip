package io.github.beinlichsimone.vacationtrip.dto.passeio;

import io.github.beinlichsimone.vacationtrip.model.Passeio;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class PasseioResumoDTO {

    private String nome;
    private String descricao;
    private String observacao;
    private String links;
    private LocalDate dataPasseio;

    public PasseioResumoDTO(Passeio passeio){
        this.nome = passeio.getNome();
        this.descricao = passeio.getDescricao();
        this.observacao = passeio.getObservacao();
        this.links = passeio.getLinks();
        this.dataPasseio = getDataPasseio();
    }
}
