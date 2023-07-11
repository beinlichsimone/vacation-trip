package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaDTO;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.repository.PessoaRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import org.modelmapper.ModelMapper;
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
@CrossOrigin(origins = {"http://localhost:4200"})
public class PessoaController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<PessoaDTO> cadastrar(@RequestBody @Valid PessoaDTO pessoaDTO, UriComponentsBuilder uriBuilder){
        Pessoa pessoa = modelMapper.map(pessoaDTO, Pessoa.class) ;
        pessoaRepository.save(pessoa);

        URI uri = uriBuilder.path("pessoa/{id}").buildAndExpand(pessoa.getId()).toUri();
        return ResponseEntity.created(uri).body(pessoaDTO);
    }

    @GetMapping("/pessoas")
    public List<Pessoa> getPessoas(){
        return pessoaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> getPessoaById(@PathVariable("id") Integer id){
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        if (pessoa.isPresent()){
            return ResponseEntity.ok(modelMapper.map(pessoa.get(), PessoaDTO.class));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Pessoa> atualizar(@PathVariable("id") Integer id, @RequestBody PessoaDTO pessoaDTO){
        Optional<Pessoa> pessoaOp = pessoaRepository.findById(id);
        if (pessoaOp.isPresent()){
            Pessoa pessoa = modelMapper.map(pessoaDTO, Pessoa.class);
            pessoa.setId(id);
            pessoaRepository.save(pessoa);
            return ResponseEntity.ok(pessoa);
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
