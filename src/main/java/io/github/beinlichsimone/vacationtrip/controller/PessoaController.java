package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaDTO;
import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaDetalheDTO;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.repository.PessoaRepository;
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
@RequestMapping("pessoa")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<PessoaDTO> cadastrar(@RequestBody @Valid PessoaDTO pessoaDTO, UriComponentsBuilder uriBuilder){
        Pessoa pessoa = pessoaDTO.converter(viagemRepository);
        pessoaRepository.save(pessoa);

        URI uri = uriBuilder.path("pessoa/{id}").buildAndExpand(pessoa.getId()).toUri();
        return ResponseEntity.created(uri).body(new PessoaDTO((pessoa)));
    }

    @GetMapping("/pessoas")
    public List<PessoaDTO> getPessoas(){
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return PessoaDTO.converterParaDTO(pessoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDetalheDTO> getPessoaById(@PathVariable("id") Integer id){
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        if (pessoa.isPresent()){
            return ResponseEntity.ok(new PessoaDetalheDTO(pessoa.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PessoaDTO> atualizar(@PathVariable("id") Integer id, @RequestBody PessoaAtualizarDTO pessoaAtualizarDTO){
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        if (pessoa.isPresent()){
            Pessoa pessoaAtualizada = pessoaAtualizarDTO.atualizar(id, pessoaRepository);
            return ResponseEntity.ok(new PessoaDTO(pessoaAtualizada));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover (@PathVariable("id") Integer id){
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        if (pessoa.isPresent()){
            pessoaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
