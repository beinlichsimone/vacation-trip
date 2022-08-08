package io.github.beinlichsimone.vacationtrip.dto.documento;

import io.github.beinlichsimone.vacationtrip.model.Documento;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import lombok.Data;

@Data
public class DocumentoResumoDTO {
    private String nome;
    private String numero;

    public DocumentoResumoDTO(Documento documento){
        this.nome = documento.getNome();
        this.numero = documento.getNumero();

    }
}
