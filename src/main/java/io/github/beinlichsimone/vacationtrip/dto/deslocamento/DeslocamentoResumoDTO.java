package io.github.beinlichsimone.vacationtrip.dto.deslocamento;

import io.github.beinlichsimone.vacationtrip.model.Deslocamento;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.model.enums.TipoVeiculoEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DeslocamentoResumoDTO {

    private String nome;
    private String local;
    private String descricao;
    private BigDecimal valor;
    private String link;
    private TipoVeiculoEnum veiculo;
    private LocalDateTime ida;
    private LocalDateTime volta;

    public DeslocamentoResumoDTO(Deslocamento deslocamento){
        this.nome = deslocamento.getNome();
        this.local = deslocamento.getLocal();
        this.descricao = deslocamento.getDescricao();
        this.valor = deslocamento.getValor();
        this.link = deslocamento.getLink();
        this.veiculo = deslocamento.getVeiculo();
        this.ida = deslocamento.getIda();
        this.volta = deslocamento.getVolta();
    }
}
