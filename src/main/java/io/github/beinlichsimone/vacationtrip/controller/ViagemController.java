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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping({"/viagens", "/viagem"})
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"}) // permite Angular e Next.js em dev
public class ViagemController {

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping({"", "/viagens"})
	@Cacheable(
			value = "listaViagens",
			key = "T(io.github.beinlichsimone.vacationtrip.config.tenancy.TenantContext).getCurrentTenant() + ':' + #p0.pageNumber + ':' + #p0.pageSize + ':' + #p0.sort"
	)
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
    public ResponseEntity<ViagemDetalheDTO> detalhe(@PathVariable("id") Integer id){
        Optional<ViagemDetalheDTO> viagem = viagemService.encontrarDetalhesPeloId(id);
        return viagem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @CacheEvict (value="listaViagens", allEntries = true)//ao chamar esse método ele irá forçar atualizar o cache. Precisa passar no value o mesmo nome do cache do listar
    public ResponseEntity<ViagemDTO> cadastrar(@RequestBody ViagemDTO viagemDTO, UriComponentsBuilder uriBuilder){

        Viagem salvo = viagemService.salvar(modelMapper.map(viagemDTO, Viagem.class));
        ViagemDTO body = modelMapper.map(salvo, ViagemDTO.class);

        URI uri = uriBuilder.path("/viagens/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(body);
    }

    @PutMapping("/{id}")
    @CacheEvict (value="listaViagens", allEntries = true)
    public ResponseEntity<Viagem> atualizar(@PathVariable("id") Integer id, @RequestBody ViagemDTO viagemDTO) {
        Optional<Viagem> viagem = viagemService.encontrarPeloId(id);
        if (viagem.isPresent()) {
            Viagem viagemAtualizada = viagemService.atualizar(id, viagemDTO);
            return ResponseEntity.ok(viagemAtualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/{id}/imagem", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    @CacheEvict(value = "listaViagens", allEntries = true)
    public ResponseEntity<ViagemDTO> uploadImagem(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) {
        try {
            Optional<Viagem> opt = viagemService.encontrarPeloId(id);
            if (opt.isEmpty()) return ResponseEntity.notFound().build();

            Viagem v = opt.get();

            Path baseDir = Path.of("uploads", "viagens");
            Files.createDirectories(baseDir);

            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "imagem";
            String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
            String filename = id + "-" + UUID.randomUUID() + ext;
            Path target = baseDir.resolve(filename);

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            String publicUrl = "/uploads/viagens/" + filename;
            v.setImagem(publicUrl);
            viagemService.salvar(v);

            return ResponseEntity.ok(modelMapper.map(v, ViagemDTO.class));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @CacheEvict (value="listaViagens", allEntries = true)
    public ResponseEntity<Void> remover(@PathVariable("id") Integer id){
        Optional<Viagem> viagem = viagemService.encontrarPeloId(id);
        if (viagem.isPresent()) {
            viagemService.deletarPeloId(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
