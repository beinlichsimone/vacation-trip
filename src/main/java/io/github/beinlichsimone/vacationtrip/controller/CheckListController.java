package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.checklist.ChecklistDTO;
import io.github.beinlichsimone.vacationtrip.services.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/checklists")
public class ChecklistController {

    @Autowired
    private ChecklistService service;

    @PostMapping
    public ResponseEntity<ChecklistDTO> create(@RequestBody @Valid ChecklistDTO dto, UriComponentsBuilder uriBuilder){
        ChecklistDTO created = service.create(dto);
        URI uri = uriBuilder.path("/checklists/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public Page<ChecklistDTO> list(Pageable pageable){
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDTO> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.of(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChecklistDTO> update(@PathVariable("id") Integer id, @RequestBody @Valid ChecklistDTO dto) {
        return ResponseEntity.of(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

 