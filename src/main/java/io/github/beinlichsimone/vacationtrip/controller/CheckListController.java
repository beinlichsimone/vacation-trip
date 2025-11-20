package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.checklist.ChecklistDTO;
import io.github.beinlichsimone.vacationtrip.model.CheckList;
import io.github.beinlichsimone.vacationtrip.repository.CheckListRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("checkList")
public class CheckListController {

    @Autowired
    private CheckListRepository checkListRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<ChecklistDTO> cadastrar(@RequestBody @Valid ChecklistDTO checklistDTO, UriComponentsBuilder uriBuilder){
        CheckList checklist = checklistDTO.converter(viagemRepository);
        checkListRepository.save(checklist);

        URI uri = uriBuilder.path("checklist/{id}").buildAndExpand(checklist.getId()).toUri();
        return ResponseEntity.created(uri).body(new ChecklistDTO(checklist));
    }

    @GetMapping("/checklists")
    public List<ChecklistDTO> getCheckLists(){
        List<CheckList> checkLists = checkListRepository.findAll();
        return checkLists.stream().map(ChecklistDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDTO> getCheckListById(@PathVariable("id") Integer id) {
        Optional<CheckList> checkList = checkListRepository.findById(id);
        if (checkList.isPresent()) {
            return ResponseEntity.ok(new ChecklistDTO(checkList.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ChecklistDTO> atualizar(@PathVariable("id") Integer id, @RequestBody ChecklistDTO checklistDTO) {
        Optional<CheckList> checkList = checkListRepository.findById(id);
        if (checkList.isPresent()) {
            CheckList checkListAtualizada = checklistDTO.converter(viagemRepository);
            checkListAtualizada.setId(id);
            checkListRepository.save(checkListAtualizada);
            return ResponseEntity.ok(new ChecklistDTO(checkListAtualizada));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover (@PathVariable("id") Integer id) {
        Optional<CheckList> checkList = checkListRepository.findById(id);
        if (checkList.isPresent()) {
            checkListRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
