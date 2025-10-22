package io.github.beinlichsimone.vacationtrip.dto.deslocamento;

import io.github.beinlichsimone.vacationtrip.model.Deslocamento;
import io.github.beinlichsimone.vacationtrip.model.Passeio;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.model.enums.TipoVeiculoEnum;
import io.github.beinlichsimone.vacationtrip.repository.DeslocamentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.PasseioRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeslocamentoDTO {

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

    public DeslocamentoDTO(Deslocamento deslocamento) {
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
    }

    public static List<DeslocamentoDTO> converterParaDTO(List<Deslocamento> deslocamentos) {
        return deslocamentos.stream().map(DeslocamentoDTO::new).collect(Collectors.toList());
    }

    public Deslocamento converter(ViagemRepository viagemRepository, PasseioRepository passeioRepository){
        Viagem viagem = viagemRepository.findById(idViagem).get();
        Passeio passeio = passeioRepository.findById(idPasseio).get();
        return new Deslocamento(id, nome, local, descricao, valor, link, veiculo, ida, volta, viagem, passeio);
    }
}
