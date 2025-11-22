package io.github.beinlichsimone.vacationtrip.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String nome;

    private String numero;

    private String observacao;

    @OneToMany( mappedBy = "documento", fetch = FetchType.EAGER )
    private List<ImagemDocumento> imagens;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    @JsonBackReference
    private Pessoa pessoa;

    public Documento(Integer id, String nome, String numero, String observacao, Pessoa pessoa) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.observacao = observacao;
        this.pessoa = pessoa;
    }
}
