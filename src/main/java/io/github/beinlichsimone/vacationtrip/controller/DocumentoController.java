package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoDTO;
import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoDetalheDTO;
import io.github.beinlichsimone.vacationtrip.services.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping({"/documentos", "/documento"})
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping
    public ResponseEntity<DocumentoDTO> cadastrar(@RequestBody @Valid DocumentoDTO documentoDTO, UriComponentsBuilder uriBuilder){
        DocumentoDTO created = documentoService.create(documentoDTO);
        URI uri = uriBuilder.path("/documentos/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping({"", "/documentos"})
    public Page<DocumentoDTO> getDocumentos(Pageable pageable){
        return documentoService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDetalheDTO> getDocumentoById(@PathVariable("id") Integer id){
        return documentoService.get(id)
                .map(dto -> ResponseEntity.ok(new DocumentoDetalheDTO(new io.github.beinlichsimone.vacationtrip.model.Documento(dto.getId(), dto.getNome(), dto.getNumero(), dto.getObservacao(), null))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoDTO> atualizar(@PathVariable("id") Integer id, @RequestBody @Valid DocumentoAtualizarDTO documentoAtualizarDTO){
        return ResponseEntity.of(documentoService.update(id, documentoAtualizarDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover (@PathVariable("id") Integer id){
        boolean deleted = documentoService.delete(id);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
