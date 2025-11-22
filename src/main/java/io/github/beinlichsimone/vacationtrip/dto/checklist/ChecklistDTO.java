package io.github.beinlichsimone.vacationtrip.dto.checklist;

import io.github.beinlichsimone.vacationtrip.model.CheckList;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChecklistDTO {
    private Integer id;
    @NotBlank
    private String nome;
    private Boolean checked;
    private String observacao;
    private Integer idViagem;

    public CheckList converter(ViagemRepository viagemRepository){
        Viagem viagem = viagemRepository.findById(idViagem).orElse(null);
        return new CheckList(id, nome, checked, observacao, viagem);
    }

    public ChecklistDTO(CheckList entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.checked = entity.getChecked();
        this.observacao = entity.getObservacao();
        this.idViagem = (entity.getViagem() != null ? entity.getViagem().getId() : null);
    }
}
