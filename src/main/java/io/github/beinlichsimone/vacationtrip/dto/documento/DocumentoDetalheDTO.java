package io.github.beinlichsimone.vacationtrip.dto.documento;

import io.github.beinlichsimone.vacationtrip.model.Documento;
import io.github.beinlichsimone.vacationtrip.model.ImagemDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentoDetalheDTO {

    private Integer id;
    @NotBlank
    private String nome;
    private String numero;
    private String observação;
    private Integer idPessoa;
    private List<ImagemDocumento> imagensDocumento;

    public DocumentoDetalheDTO(Documento documento) {
        this.id = documento.getId();
        this.nome = documento.getNome();
        this.numero = documento.getNumero();
        this.observação = documento.getObservação();
        this.idPessoa = documento.getPessoa().getId();
        this.imagensDocumento = new ArrayList<>();
        this.imagensDocumento.addAll(documento.getImagens().stream().map(ImagemDocumento::new).collect(Collectors.toList()));
    }
}
