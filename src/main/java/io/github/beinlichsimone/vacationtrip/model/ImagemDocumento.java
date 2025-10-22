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
public class ImagemDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    private String nome;

    @ManyToOne
    @JoinColumn(name = "documento_id")
    private Documento documento;

    public ImagemDocumento(ImagemDocumento imagemDocumento) {
        this.nome = imagemDocumento.getNome();
    }
}
