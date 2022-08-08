package io.github.beinlichsimone.vacationtrip.dto.pessoa;

import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoDTO;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
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
public class PessoaDetalheDTO {

    private Integer id;
    @NotBlank
    private String nome;
    private Integer cpf;
    private Integer idViagem;
    private List<DocumentoDTO> documentos;

    public PessoaDetalheDTO(Pessoa pessoa) {
        this.id = pessoa.getId();
        this.nome = pessoa.getNome();
        this.cpf = pessoa.getCpf();
        this.idViagem = pessoa.getViagem().getId();
        this.documentos = new ArrayList<>();
        this.documentos.addAll(pessoa.getDocumentos().stream().map(DocumentoDTO::new).collect(Collectors.toList()));
    }
}
