package io.github.beinlichsimone.vacationtrip.dto.pessoa;

import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import lombok.Data;

@Data
public class PessoaResumoDTO {
    private String nome;
    private String cpf;

    public PessoaResumoDTO(Pessoa pessoa){
        this.nome = pessoa.getNome();
        this.cpf = pessoa.getCpf();

    }
}
