package io.github.beinlichsimone.vacationtrip.dto.pessoa;

import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.PessoaRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PessoaAtualizarDTO {

    @NotBlank
    private String nome;
    private Integer cpf;
    private LocalDate dtNascimento;
    private String endereco;
    private Integer telefone;
    private String email;

    public Pessoa atualizar(Integer Id, PessoaRepository pessoaRepository){
        Optional<Pessoa> pessoaOp = pessoaRepository.findById(Id);
        Pessoa pessoa = pessoaOp.get();

        pessoa.setNome(this.nome);
        pessoa.setCpf(this.cpf);
        pessoa.setDtNascimento(this.dtNascimento);
        pessoa.setEndereco(this.endereco);
        pessoa.setTelefone(this.telefone);
        pessoa.setEmail(this.email);

        return pessoa;
    }

}
