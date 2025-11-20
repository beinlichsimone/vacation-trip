package io.github.beinlichsimone.vacationtrip.services;

import io.github.beinlichsimone.vacationtrip.dto.checklist.ChecklistDTO;
import io.github.beinlichsimone.vacationtrip.model.CheckList;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.CheckListRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ChecklistService {

    @Autowired
    private CheckListRepository checkListRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @Transactional
    public ChecklistDTO create(ChecklistDTO dto) {
        CheckList entity = new CheckList();
        entity.setChecked(dto.getChecked());
        entity.setObservacao(dto.getObservacao());
        if (dto.getIdViagem() != null) {
            Optional<Viagem> viagem = viagemRepository.findById(dto.getIdViagem());
            viagem.ifPresent(entity::setViagem);
        }
        CheckList saved = checkListRepository.save(entity);
        return new ChecklistDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<ChecklistDTO> list(Pageable pageable) {
        return checkListRepository.findAll(pageable).map(ChecklistDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<ChecklistDTO> get(Integer id) {
        return checkListRepository.findById(id).map(ChecklistDTO::new);
    }

    @Transactional
    public Optional<ChecklistDTO> update(Integer id, ChecklistDTO dto) {
        Optional<CheckList> maybeEntity = checkListRepository.findById(id);
        if (maybeEntity.isEmpty()) {
            return Optional.empty();
        }
        CheckList entity = maybeEntity.get();
        entity.setChecked(dto.getChecked());
        entity.setObservacao(dto.getObservacao());
        if (dto.getIdViagem() != null) {
            Optional<Viagem> viagem = viagemRepository.findById(dto.getIdViagem());
            entity.setViagem(viagem.orElse(null));
        } else {
            entity.setViagem(null);
        }
        CheckList updated = checkListRepository.save(entity);
        return Optional.of(new ChecklistDTO(updated));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!checkListRepository.existsById(id)) {
            return false;
        }
        checkListRepository.deleteById(id);
        return true;
    }
}


