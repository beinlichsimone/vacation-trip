package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping({"/pessoas", "/pessoa"})
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class PessoaController {

    @Autowired
    private io.github.beinlichsimone.vacationtrip.services.PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<PessoaDTO> cadastrar(@RequestBody @Valid PessoaDTO pessoaDTO, UriComponentsBuilder uriBuilder){
        PessoaDTO created = pessoaService.create(pessoaDTO);
        URI uri = uriBuilder.path("/pessoas/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping({"", "/pessoas"})
    public org.springframework.data.domain.Page<PessoaDTO> getPessoas(org.springframework.data.domain.Pageable pageable){
        return pessoaService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> getPessoaById(@PathVariable("id") Integer id){
        return ResponseEntity.of(pessoaService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizar(@PathVariable("id") Integer id, @RequestBody @Valid PessoaDTO pessoaDTO){
        return ResponseEntity.of(pessoaService.update(id, pessoaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover (@PathVariable("id") Integer id){
        boolean deleted = pessoaService.delete(id);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

}
