package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.viagem.ViagemAtualizaDTO;
import io.github.beinlichsimone.vacationtrip.dto.viagem.ViagemDetalheDTO;
import io.github.beinlichsimone.vacationtrip.dto.viagem.ViagemCadastroDTO;
import io.github.beinlichsimone.vacationtrip.dto.viagem.ViagemRetornoDTO;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/viagem")
public class ViagemController {

    @Autowired
    private ViagemRepository viagemRepository;

    @GetMapping("/viagens")
    @Cacheable(value="listaViagens")
    public Page<ViagemRetornoDTO> getViagens(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable paginacao){
    // Da forma acima está retornando o erro "No primary or single unique constructor found for interface org.springframework.data.domain.Pageable"
    // O cache não deve ser usado em tabelas que são muito utilizadas, pois existe um custo de processamento armazenar e apagar a memória cache. Deve ser estudado onde faz sentido utilizar o cache
    //public Page<ViagemRetornoDTO> getViagens(@RequestParam int pagina, @RequestParam int quantidade,
    //                                         @RequestParam String ordenacao){ //paginação e ordenação

        //Pageable paginacao = PageRequest.of(pagina, quantidade, Sort.Direction.ASC, ordenacao);
        Page<Viagem> viagens = viagemRepository.findAll(paginacao);
        return ViagemRetornoDTO.converterParaDTO(viagens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViagemDetalheDTO> buscaPorId(@PathVariable("id") Integer id){
        Optional<Viagem> viagem = viagemRepository.findById(id);
        if (viagem.isPresent()){
            return ResponseEntity.ok(new ViagemDetalheDTO(viagem.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Transactional
    @CacheEvict (value="listaViagens", allEntries = true)//ao chamar esse método ele irá forçar atualizar o cache. Precisa passar no value o mesmo nome do cache do listar
    public ResponseEntity<ViagemCadastroDTO> cadastrar(@RequestBody ViagemCadastroDTO viagemDTO, UriComponentsBuilder uriBuilder){

        Viagem viagem = viagemDTO.converter();
        viagemRepository.save(viagem);

        URI uri = uriBuilder.path("viagem/{id}").buildAndExpand(viagem.getId()).toUri();
        return ResponseEntity.created(uri).body(new ViagemCadastroDTO((viagem)));
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict (value="listaViagens", allEntries = true)
    public ResponseEntity<ViagemRetornoDTO> atualizar(@PathVariable("id") Integer id, @RequestBody ViagemAtualizaDTO viagemDTO) {
        Optional<Viagem> viagem = viagemRepository.findById(id);
        if (viagem.isPresent()) {
            Viagem viagemAtualizada = viagemDTO.atualizar(id, viagemRepository);
            return ResponseEntity.ok(new ViagemRetornoDTO(viagemAtualizada));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict (value="listaViagens", allEntries = true)
    public ResponseEntity remover(@PathVariable("id") Integer id){
        Optional<Viagem> viagem = viagemRepository.findById(id);
        if (viagem.isPresent()) {
            viagemRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
