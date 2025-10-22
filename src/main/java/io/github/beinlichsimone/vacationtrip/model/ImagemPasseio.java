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
public class ImagemPasseio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String nome;

    @ManyToOne
    @JoinColumn(name = "passeio_id")
    private Passeio passeio;


    public ImagemPasseio(ImagemPasseio imagemPasseio) {
        this.nome = imagemPasseio.getNome();
    }
}
