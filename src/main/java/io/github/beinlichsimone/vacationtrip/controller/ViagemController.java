package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.viagem.ViagemDTO;
import io.github.beinlichsimone.vacationtrip.dto.viagem.ViagemDetalheDTO;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.services.ViagemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/viagem")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"}) // permite Angular e Next.js em dev
public class ViagemController {

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/viagens")
    @Cacheable(value="listaViagens")
    public Page<ViagemDTO> getViagens(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable paginacao){
    // O cache não deve ser usado em tabelas que são muito utilizadas, pois existe um custo de processamento armazenar e apagar a memória cache. Deve ser estudado onde faz sentido utilizar o cache

        Page<Viagem> viagens = viagemService.findAllPaginacao(paginacao);
        return ViagemDTO.converterParaDTOcomPage(viagens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViagemDTO> buscaPorId(@PathVariable("id") Integer id){
        Optional<Viagem> viagem = viagemService.encontrarPeloId(id);
        if (viagem.isPresent()){
            return ResponseEntity.ok(modelMapper.map(viagem.get(), ViagemDTO.class));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/detalhe")
    @Transactional(readOnly = true)
    public ResponseEntity<ViagemDetalheDTO> detalhe(@PathVariable("id") Integer id){
        Optional<Viagem> viagem = viagemService.encontrarDetalhesPeloId(id);
        if (viagem.isPresent()){
            return ResponseEntity.ok(new ViagemDetalheDTO(viagem.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Transactional
    @CacheEvict (value="listaViagens", allEntries = true)//ao chamar esse método ele irá forçar atualizar o cache. Precisa passar no value o mesmo nome do cache do listar
    public ResponseEntity<ViagemDTO> cadastrar(@RequestBody ViagemDTO viagemDTO, UriComponentsBuilder uriBuilder){

        viagemService.salvar(modelMapper.map(viagemDTO, Viagem.class));

        URI uri = uriBuilder.path("viagem/{id}").buildAndExpand(viagemDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(viagemDTO);
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict (value="listaViagens", allEntries = true)
    public ResponseEntity<Viagem> atualizar(@PathVariable("id") Integer id, @RequestBody ViagemDTO viagemDTO) {
        Optional<Viagem> viagem = viagemService.encontrarPeloId(id);
        if (viagem.isPresent()) {
            Viagem viagemAtualizada = viagemService.atualizar(id, viagemDTO);
            return ResponseEntity.ok(viagemAtualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict (value="listaViagens", allEntries = true)
    public ResponseEntity<Void> remover(@PathVariable("id") Integer id){
        Optional<Viagem> viagem = viagemService.encontrarPeloId(id);
        if (viagem.isPresent()) {
            viagemService.deletarPeloId(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
