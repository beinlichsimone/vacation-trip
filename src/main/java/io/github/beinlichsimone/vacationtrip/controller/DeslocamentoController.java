package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoDTO;
import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoDetalheDTO;
import io.github.beinlichsimone.vacationtrip.model.Deslocamento;
import io.github.beinlichsimone.vacationtrip.repository.DeslocamentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.PasseioRepository;
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
@RequestMapping("deslocamento")
public class DeslocamentoController {

    @Autowired
    private DeslocamentoRepository deslocamentoRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private PasseioRepository passeioRepository;


    @PostMapping
    @Transactional
    public ResponseEntity<DeslocamentoDTO> cadastrar(@RequestBody @Valid DeslocamentoDTO deslocamentoDTO, UriComponentsBuilder uriBuilder){
        Deslocamento deslocamento = deslocamentoDTO.converter(viagemRepository, passeioRepository);
        deslocamentoRepository.save(deslocamento);

        URI uri = uriBuilder.path("deslocamento/{id}").buildAndExpand(deslocamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new DeslocamentoDTO((deslocamento)));
    }

    @GetMapping("/deslocamentos")
    public List<DeslocamentoDTO> getDeslocamentos(){
        List<Deslocamento> deslocamentos = deslocamentoRepository.findAll();
        return DeslocamentoDTO.converterParaDTO(deslocamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeslocamentoDetalheDTO> getDeslocamentoById(@PathVariable("id") Integer id){
        Optional<Deslocamento> deslocamento = deslocamentoRepository.findById(id);
        if (deslocamento.isPresent()){
            return ResponseEntity.ok(new DeslocamentoDetalheDTO(deslocamento.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DeslocamentoDTO> atualizar(@PathVariable("id") Integer id, @RequestBody DeslocamentoAtualizarDTO deslocamentoAtualizarDTO){
        Optional<Deslocamento> deslocamento = deslocamentoRepository.findById(id);
        if (deslocamento.isPresent()){
            Deslocamento deslocamentoAtualizada = deslocamentoAtualizarDTO.atualizar(id, deslocamentoRepository);
            return ResponseEntity.ok(new DeslocamentoDTO(deslocamentoAtualizada));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover (@PathVariable("id") Integer id){
        Optional<Deslocamento> deslocamento = deslocamentoRepository.findById(id);
        if (deslocamento.isPresent()){
            deslocamentoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
