package io.github.beinlichsimone.vacationtrip.dto.checklist;

import io.github.beinlichsimone.vacationtrip.model.CheckList;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChecklistDTO {
    private Integer id;
    private Boolean check;
    private String observacao;
    private Integer idViagem;

    public CheckList converter(ViagemRepository viagemRepository){
        Viagem viagem = viagemRepository.findById(idViagem).get();
        return new CheckList(id, check, observacao, viagem);
    }

    public ChecklistDTO(CheckList entity) {
        this.id = entity.getId();
        this.check = entity.getCheck();
        this.observacao = entity.getObservacao();
        this.idViagem = (entity.getViagem() != null ? entity.getViagem().getId() : null);
    }
}
