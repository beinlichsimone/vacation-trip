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
public class ImagemDeslocamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String nome;

    @ManyToOne
    @JoinColumn(name = "deslocamento_id")
    private Deslocamento deslocamento;

    public ImagemDeslocamento(ImagemDeslocamento imagemDeslocamento) {
        this.nome = imagemDeslocamento.getNome();
    }
}
