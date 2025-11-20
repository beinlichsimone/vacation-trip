package io.github.beinlichsimone.vacationtrip.dto.viagem;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoDTO;
import io.github.beinlichsimone.vacationtrip.dto.passeio.PasseioDTO;
import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaDTO;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViagemDetalheDTO {

    private Integer id;
    private String nome;
    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataIda;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataVolta;

    private List<PessoaDTO> pessoas;
    private List<PasseioDTO> passeios;
    private List<DeslocamentoDTO> deslocamentos;

    public ViagemDetalheDTO(Viagem viagem) {
        this.id = viagem.getId();
        this.nome = viagem.getNome();
        this.descricao = viagem.getDescricao();
        this.dataIda = viagem.getDataIda();
        this.dataVolta = viagem.getDataVolta();
        this.pessoas = viagem.getPessoas() == null ? List.of() : viagem.getPessoas()
                .stream()
                .map(p -> {
                    PessoaDTO dto = new PessoaDTO();
                    dto.setId(p.getId() == null ? null : String.valueOf(p.getId()));
                    dto.setNome(p.getNome());
                    dto.setCpf(p.getCpf());
                    dto.setEmail(p.getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
        this.passeios = viagem.getPasseios() == null ? List.of() : viagem.getPasseios()
                .stream()
                .map(PasseioDTO::new)
                .collect(Collectors.toList());
        this.deslocamentos = viagem.getDeslocamentos() == null ? List.of() : viagem.getDeslocamentos()
                .stream()
                .map(DeslocamentoDTO::new)
                .collect(Collectors.toList());
    }
}


