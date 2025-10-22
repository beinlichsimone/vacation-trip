package io.github.beinlichsimone.vacationtrip.dto.deslocamento;

import io.github.beinlichsimone.vacationtrip.model.Deslocamento;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.model.enums.TipoVeiculoEnum;
import io.github.beinlichsimone.vacationtrip.repository.DeslocamentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.PessoaRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeslocamentoAtualizarDTO {

    @NotBlank
    private String nome;
    private String local;
    private String descricao;
    private BigDecimal valor;
    private String link;
    private TipoVeiculoEnum veiculo;
    private LocalDateTime ida;
    private LocalDateTime volta;

    public Deslocamento atualizar(Integer Id, DeslocamentoRepository deslocamentoRepository){
        Optional<Deslocamento> deslocamentoOp = deslocamentoRepository.findById(Id);
        Deslocamento deslocamento = deslocamentoOp.get();

        deslocamento.setNome(this.nome);
        deslocamento.setLocal(this.local);
        deslocamento.setDescricao(this.descricao);
        deslocamento.setValor(this.valor);
        deslocamento.setLink(this.link);
        deslocamento.setVeiculo(this.veiculo);
        deslocamento.setIda(this.ida);
        deslocamento.setVolta(this.volta);

        return deslocamento;
    }

}
