package io.github.beinlichsimone.vacationtrip.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Passeio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String nome;

    private String descricao;

    private String observacao;

    private String links;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataPasseio;

    @OneToMany( mappedBy = "passeio", fetch = FetchType.EAGER )
    @Fetch(FetchMode.SUBSELECT)
    private List<ImagemPasseio> imagens;

    @OneToMany( mappedBy = "passeio", fetch = FetchType.EAGER )
    @Fetch(FetchMode.SUBSELECT)
    private List<Deslocamento> deslocamentos;

    @ManyToOne
    @JoinColumn(name = "viagem_id")
    private Viagem viagem;

    public Passeio(Integer id, String nome, String descricao, String observacao, String links, LocalDate dataPasseio, Viagem viagem) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.observacao = observacao;
        this.links = links;
        this.dataPasseio = dataPasseio;
        this.viagem = viagem;
    }
}
