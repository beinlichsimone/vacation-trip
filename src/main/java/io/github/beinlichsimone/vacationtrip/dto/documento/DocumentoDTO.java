package io.github.beinlichsimone.vacationtrip.dto.documento;

import io.github.beinlichsimone.vacationtrip.model.Documento;
import io.github.beinlichsimone.vacationtrip.model.ImagemDeslocamento;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.DocumentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.PessoaRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.Document;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentoDTO {

    private Integer id;
    @NotBlank
    private String nome;
    private String numero;
    private String observação;
    private Integer idPessoa;

    public DocumentoDTO(Documento documento) {
        this.id = documento.getId();
        this.nome = documento.getNome();
        this.numero = documento.getNumero();
        this.idPessoa = documento.getPessoa().getId();
    }


    public static List<DocumentoDTO> converterParaDTO(List<Documento> documentos) {
        return documentos.stream().map(DocumentoDTO::new).collect(Collectors.toList());
    }

    public Documento converter(PessoaRepository pessoaRepository){
        Pessoa pessoa = pessoaRepository.findById(idPessoa).get();
        return new Documento(id, nome, numero, observação, pessoa);
    }
}
