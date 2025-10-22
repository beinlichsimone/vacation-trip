package io.github.beinlichsimone.vacationtrip.dto.passeio;

import io.github.beinlichsimone.vacationtrip.model.Passeio;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.PasseioRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PasseioDTO {

    private Integer id;
    @NotBlank
    private String nome;
    private String descricao;
    private String observação;
    private String links;
    private LocalDate dataPasseio;
    private Integer idViagem;

    public PasseioDTO(Passeio passeio) {
        this.id = passeio.getId();
        this.nome = passeio.getNome();
        this.descricao = passeio.getDescricao();
        this.links = passeio.getLinks();
        this.dataPasseio = passeio.getDataPasseio();
        this.idViagem = passeio.getViagem().getId();
    }

    public static List<PasseioDTO> converterParaDTO(List<Passeio> passeios) {
        return passeios.stream().map(PasseioDTO::new).collect(Collectors.toList());
    }

    public Passeio converter(ViagemRepository viagemRepository){
        Viagem viagem = viagemRepository.findById(idViagem).get();
        return new Passeio(id, nome, descricao, observação, links, dataPasseio, viagem);
    }
}
