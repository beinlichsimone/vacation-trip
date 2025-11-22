package io.github.beinlichsimone.vacationtrip.dto.documento;

import io.github.beinlichsimone.vacationtrip.model.Documento;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.repository.DocumentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.PessoaRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentoAtualizarDTO {

    @NotBlank
    private String nome;
    private String numero;
    private String observacao;

    public Documento atualizar(Integer Id, DocumentoRepository documentoRepository){
        Optional<Documento> documentoOp = documentoRepository.findById(Id);
        Documento documento = documentoOp.get();

        documento.setNome(this.nome);
        documento.setNumero(this.numero);
        documento.setObservacao(this.observacao);

        return documento;
    }

}
