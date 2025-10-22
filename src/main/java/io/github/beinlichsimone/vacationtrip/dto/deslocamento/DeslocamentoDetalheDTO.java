package io.github.beinlichsimone.vacationtrip.dto.deslocamento;

import io.github.beinlichsimone.vacationtrip.model.Deslocamento;
import io.github.beinlichsimone.vacationtrip.model.ImagemDeslocamento;
import io.github.beinlichsimone.vacationtrip.model.enums.TipoVeiculoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeslocamentoDetalheDTO {

    private Integer id;
    @NotBlank
    private String nome;
    private String local;
    private String descricao;
    private BigDecimal valor;
    private String link;
    private TipoVeiculoEnum veiculo;
    private LocalDateTime ida;
    private LocalDateTime volta;
    private Integer idPasseio;
    private Integer idViagem;

    private List<ImagemDeslocamento> imagensDeslocamento;

    public DeslocamentoDetalheDTO(Deslocamento deslocamento) {
        this.id = deslocamento.getId();
        this.nome = deslocamento.getNome();
        this.local = deslocamento.getLocal();
        this.descricao = deslocamento.getDescricao();
        this.valor = deslocamento.getValor();
        this.link = deslocamento.getLink();
        this.veiculo = deslocamento.getVeiculo();
        this.ida = deslocamento.getIda();
        this.volta = deslocamento.getVolta();
        this.idViagem = deslocamento.getViagem().getId();
        this.idPasseio = deslocamento.getPasseio().getId();

        this.imagensDeslocamento = new ArrayList<>();
        this.imagensDeslocamento.addAll(deslocamento.getImagens().stream().map(ImagemDeslocamento::new).collect(Collectors.toList()));
    }
}
