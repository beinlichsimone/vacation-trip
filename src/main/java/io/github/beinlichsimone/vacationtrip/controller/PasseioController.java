package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.passeio.PasseioDTO;
import io.github.beinlichsimone.vacationtrip.dto.passeio.PasseioAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.passeio.PasseioDetalheDTO;
import io.github.beinlichsimone.vacationtrip.services.PasseioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping({"/passeios", "/passeio"})
public class PasseioController {

    @Autowired
    private PasseioService passeioService;

    @PostMapping
    public ResponseEntity<PasseioDTO> cadastrar(@RequestBody @Valid PasseioDTO passeioDTO, UriComponentsBuilder uriBuilder){
        PasseioDTO created = passeioService.create(passeioDTO);
        URI uri = uriBuilder.path("/passeios/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping({"", "/passeios"})
    public Page<PasseioDTO> getPasseios(Pageable pageable){
        return passeioService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasseioDetalheDTO> getPasseioById(@PathVariable("id") Integer id){
        return passeioService.get(id)
                .map(dto -> ResponseEntity.ok(new PasseioDetalheDTO(new io.github.beinlichsimone.vacationtrip.model.Passeio(dto.getId(), dto.getNome(), dto.getDescricao(), dto.getObservacao(), dto.getLinks(), dto.getDataPasseio(), null))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PasseioDTO> atualizar(@PathVariable("id") Integer id, @RequestBody @Valid PasseioAtualizarDTO passeioAtualizarDTO){
        return ResponseEntity.of(passeioService.update(id, passeioAtualizarDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover (@PathVariable("id") Integer id){
        boolean deleted = passeioService.delete(id);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
