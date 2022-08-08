package io.github.beinlichsimone.vacationtrip.dto.pessoa;

import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PessoaDTO {

    private Integer id;
    @NotBlank
    private String nome;
    private Integer cpf;
    private Integer idViagem;

    public PessoaDTO(Pessoa pessoa) {
        this.id = pessoa.getId();
        this.nome = pessoa.getNome();
        this.cpf = pessoa.getCpf();
        this.idViagem = pessoa.getViagem().getId();
    }

    public static List<PessoaDTO> converterParaDTO(List<Pessoa> pessoas) {
        return pessoas.stream().map(PessoaDTO::new).collect(Collectors.toList());
    }

    public Pessoa converter(ViagemRepository viagemRepository){
        Viagem viagem = viagemRepository.findById(idViagem).get();
        return new Pessoa(id, nome, cpf, viagem);
    }
}
