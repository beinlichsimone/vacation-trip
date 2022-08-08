package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoDTO;
import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoDetalheDTO;
import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaDTO;
import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaDetalheDTO;
import io.github.beinlichsimone.vacationtrip.model.Documento;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.repository.DocumentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.PasseioRepository;
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
@RequestMapping("documento")
public class DocumentoController {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DocumentoDTO> cadastrar(@RequestBody @Valid DocumentoDTO documentoDTO, UriComponentsBuilder uriBuilder){
        Documento documento = documentoDTO.converter(pessoaRepository);
        documentoRepository.save(documento);

        URI uri = uriBuilder.path("documento/{id}").buildAndExpand(documento.getId()).toUri();
        return ResponseEntity.created(uri).body(new DocumentoDTO((documento)));
    }

    @GetMapping("/documentos")
    public List<DocumentoDTO> getDocumentos(){
        List<Documento> documentos = documentoRepository.findAll();
        return DocumentoDTO.converterParaDTO(documentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDetalheDTO> getDocumentoById(@PathVariable("id") Integer id){
        Optional<Documento> documento = documentoRepository.findById(id);
        if (documento.isPresent()){
            return ResponseEntity.ok(new DocumentoDetalheDTO(documento.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DocumentoDTO> atualizar(@PathVariable("id") Integer id, @RequestBody DocumentoAtualizarDTO documentoAtualizarDTO){
        Optional<Documento> documento = documentoRepository.findById(id);
        if (documento.isPresent()){
            Documento documentoAtualizada = documentoAtualizarDTO.atualizar(id, documentoRepository);
            return ResponseEntity.ok(new DocumentoDTO(documentoAtualizada));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover (@PathVariable("id") Integer id){
        Optional<Documento> documento = documentoRepository.findById(id);
        if (documento.isPresent()){
            documentoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
