package io.github.beinlichsimone.vacationtrip.dto.viagem;

import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaResumoDTO;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ViagemDetalheDTO {
    private Integer Id;
    private String nome;
    private String descricao;
    private LocalDate dataIda;
    private LocalDate dataVolta;
    private List<PessoaResumoDTO> pessoas;

    public ViagemDetalheDTO(Viagem viagem) {
        this.Id = viagem.getId();
        this.nome = viagem.getNome();
        this.descricao = viagem.getDescricao();
        this.dataIda = viagem.getDataIda();
        this.dataVolta = viagem.getDataVolta();
        this.pessoas = new ArrayList<>();
        this.pessoas.addAll(viagem.getPessoas().stream().map(PessoaResumoDTO::new).collect(Collectors.toList()));
    }
}
