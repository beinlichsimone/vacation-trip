package io.github.beinlichsimone.vacationtrip.dto.passeio;

import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoDTO;
import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoDetalheDTO;
import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoResumoDTO;
import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoDTO;
import io.github.beinlichsimone.vacationtrip.model.Deslocamento;
import io.github.beinlichsimone.vacationtrip.model.ImagemPasseio;
import io.github.beinlichsimone.vacationtrip.model.Passeio;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PasseioDetalheDTO {

    private Integer id;
    private String nome;
    private String descricao;
    private String observação;
    private String links;
    private LocalDate dataPasseio;
    private Integer idViagem;
    private List<ImagemPasseio> imagens;
    private List<Deslocamento> deslocamentos;

    public PasseioDetalheDTO(Passeio passeio) {
        this.id = passeio.getId();
        this.nome = passeio.getNome();
        this.descricao = passeio.getDescricao();
        this.observação = passeio.getObservacao();
        this.links = passeio.getLinks();
        this.dataPasseio = passeio.getDataPasseio();
        this.idViagem = passeio.getViagem().getId();
        this.imagens = new ArrayList<>();
        this.imagens.addAll(passeio.getImagens().stream().map(ImagemPasseio::new).collect(Collectors.toList()));

        this.deslocamentos = new ArrayList<>();
        this.deslocamentos.addAll(passeio.getDeslocamentos().stream().map(Deslocamento::new).collect(Collectors.toList()));
    }
}
