package io.github.beinlichsimone.vacationtrip.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String nome;

    private Boolean check;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "viagem_id")
    private Viagem viagem;

    public CheckList(Integer id, Boolean check, String observacao, Viagem viagem) {
        this.id = id;
        this.check = check;
        this.observacao = observacao;
        this.viagem = viagem;
    }
}
