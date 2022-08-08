package io.github.beinlichsimone.vacationtrip.model;

import io.github.beinlichsimone.vacationtrip.model.enums.TipoVeiculoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Deslocamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String nome;

    private String local;

    private String descricao;

    private BigDecimal valor;

    private String link;

    @Enumerated(EnumType.STRING)
    private TipoVeiculoEnum veiculo;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime ida;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime volta;

    @OneToMany( mappedBy = "deslocamento", fetch = FetchType.EAGER )
    private List<ImagemDeslocamento> imagens;

    @ManyToOne
    @JoinColumn(name = "passeio_id")
    private Passeio passeio;

    @ManyToOne
    @JoinColumn(name = "viagem_id")
    private Viagem viagem;

    public Deslocamento(Integer id, String nome, String local, String descricao, BigDecimal valor, String link,
                        TipoVeiculoEnum veiculo, LocalDateTime ida, LocalDateTime volta, Viagem viagem, Passeio passeio) {
        this.id = id;
        this.nome = nome;
        this.local = local;
        this.descricao = descricao;
        this.valor = valor;
        this.link = link;
        this.veiculo = veiculo;
        this.ida = ida;
        this.volta = volta;
        this.viagem = viagem;
        this.passeio = passeio;
    }

    public Deslocamento(Deslocamento deslocamento) {
        this.id = deslocamento.getId();
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
