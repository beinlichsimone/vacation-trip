package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaDTO;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("pessoa")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
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
        if (pessoaDTO.getIdViagem() != null) {
            Optional<Viagem> viagem = viagemRepository.findById(pessoaDTO.getIdViagem());
            viagem.ifPresent(pessoa::setViagem);
        } else {
            pessoa.setViagem(null);
        }
        pessoaRepository.save(pessoa);

        URI uri = uriBuilder.path("pessoa/{id}").buildAndExpand(pessoa.getId()).toUri();
        pessoaDTO.setId(String.valueOf(pessoa.getId()));
        if (pessoa.getViagem() != null) {
            pessoaDTO.setIdViagem(pessoa.getViagem().getId());
        }
        return ResponseEntity.created(uri).body(pessoaDTO);
    }

    @GetMapping("/pessoas")
    public List<PessoaDTO> getPessoas(){
        return pessoaRepository.findAll()
                .stream()
                .map(p -> {
                    PessoaDTO dto = modelMapper.map(p, PessoaDTO.class);
                    if (p.getViagem() != null) {
                        dto.setIdViagem(p.getViagem().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> getPessoaById(@PathVariable("id") Integer id){
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        if (pessoa.isPresent()){
            PessoaDTO dto = modelMapper.map(pessoa.get(), PessoaDTO.class);
            if (pessoa.get().getViagem() != null) {
                dto.setIdViagem(pessoa.get().getViagem().getId());
            }
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PessoaDTO> atualizar(@PathVariable("id") Integer id, @RequestBody PessoaDTO pessoaDTO){
        Optional<Pessoa> pessoaOp = pessoaRepository.findById(id);
        if (pessoaOp.isPresent()){
            Pessoa pessoa = modelMapper.map(pessoaDTO, Pessoa.class);
            pessoa.setId(id);
            if (pessoaDTO.getIdViagem() != null) {
                Optional<Viagem> viagem = viagemRepository.findById(pessoaDTO.getIdViagem());
                viagem.ifPresent(pessoa::setViagem);
            } else {
                pessoa.setViagem(null);
            }
            pessoaRepository.save(pessoa);
            PessoaDTO dto = new PessoaDTO(String.valueOf(pessoa.getId()), pessoa.getNome(), pessoa.getCpf(), pessoa.getEmail(), pessoa.getViagem() != null ? pessoa.getViagem().getId() : null);
            return ResponseEntity.ok(dto);
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
