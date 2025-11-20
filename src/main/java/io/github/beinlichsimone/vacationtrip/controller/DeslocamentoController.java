package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoDTO;
import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoDetalheDTO;
import io.github.beinlichsimone.vacationtrip.services.DeslocamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping({"/deslocamentos", "/deslocamento"})
public class DeslocamentoController {

    @Autowired
    private DeslocamentoService deslocamentoService;

    @PostMapping
    public ResponseEntity<DeslocamentoDTO> cadastrar(@RequestBody @Valid DeslocamentoDTO deslocamentoDTO, UriComponentsBuilder uriBuilder){
        DeslocamentoDTO created = deslocamentoService.create(deslocamentoDTO);
        URI uri = uriBuilder.path("/deslocamentos/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping({"", "/deslocamentos"})
    public Page<DeslocamentoDTO> getDeslocamentos(Pageable pageable){
        return deslocamentoService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeslocamentoDetalheDTO> getDeslocamentoById(@PathVariable("id") Integer id){
        return deslocamentoService.get(id)
                .map(dto -> ResponseEntity.ok(new DeslocamentoDetalheDTO(new io.github.beinlichsimone.vacationtrip.model.Deslocamento(dto.getId(), dto.getNome(), dto.getLocal(), dto.getDescricao(), dto.getValor(), dto.getLink(), dto.getVeiculo(), dto.getIda(), dto.getVolta(), null, null))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeslocamentoDTO> atualizar(@PathVariable("id") Integer id, @RequestBody @Valid DeslocamentoAtualizarDTO deslocamentoAtualizarDTO){
        return ResponseEntity.of(deslocamentoService.update(id, deslocamentoAtualizarDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover (@PathVariable("id") Integer id){
        boolean deleted = deslocamentoService.delete(id);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

}
