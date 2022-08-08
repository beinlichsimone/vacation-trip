package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.passeio.PasseioDTO;
import io.github.beinlichsimone.vacationtrip.dto.passeio.PasseioAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.passeio.PasseioDetalheDTO;
import io.github.beinlichsimone.vacationtrip.model.Passeio;
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
@RequestMapping("passeio")
public class PasseioController {

    @Autowired
    private PasseioRepository passeioRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<PasseioDTO> cadastrar(@RequestBody @Valid PasseioDTO passeioDTO, UriComponentsBuilder uriBuilder){
        Passeio passeio = passeioDTO.converter(viagemRepository);
        passeioRepository.save(passeio);

        URI uri = uriBuilder.path("passeio/{id}").buildAndExpand(passeio.getId()).toUri();
        return ResponseEntity.created(uri).body(new PasseioDTO((passeio)));
    }

    @GetMapping("/passeios")
    public List<PasseioDTO> getPasseios(){
        List<Passeio> passeios = passeioRepository.findAll();
        return PasseioDTO.converterParaDTO(passeios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasseioDetalheDTO> getPasseioById(@PathVariable("id") Integer id){
        Optional<Passeio> passeio = passeioRepository.findById(id);
        if (passeio.isPresent()){
            return ResponseEntity.ok(new PasseioDetalheDTO(passeio.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PasseioDTO> atualizar(@PathVariable("id") Integer id, @RequestBody PasseioAtualizarDTO passeioAtualizarDTO){
        Optional<Passeio> passeio = passeioRepository.findById(id);
        if (passeio.isPresent()){
            Passeio passeioAtualizada = passeioAtualizarDTO.atualizar(id, passeioRepository);
            return ResponseEntity.ok(new PasseioDTO(passeioAtualizada));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover (@PathVariable("id") Integer id){
        Optional<Passeio> passeio = passeioRepository.findById(id);
        if (passeio.isPresent()){
            passeioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
