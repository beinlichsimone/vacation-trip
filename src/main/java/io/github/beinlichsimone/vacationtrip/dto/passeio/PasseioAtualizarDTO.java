package io.github.beinlichsimone.vacationtrip.dto.passeio;

import io.github.beinlichsimone.vacationtrip.model.Passeio;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.repository.PasseioRepository;
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
public class PasseioAtualizarDTO {

    @NotBlank
    private String nome;
    private String descricao;
    private String observacao;
    private String links;
    private LocalDate dataPasseio;

    public Passeio atualizar(Integer Id, PasseioRepository passeioRepository){
        Optional<Passeio> passeioOp = passeioRepository.findById(Id);
        Passeio passeio = passeioOp.get();

        passeio.setNome(this.nome);
        passeio.setDescricao(this.descricao);
        passeio.setObservacao(this.observacao);
        passeio.setLinks(this.links);
        passeio.setDataPasseio(this.dataPasseio);

        return passeio;
    }

}
