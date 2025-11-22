package io.github.beinlichsimone.vacationtrip.services;

import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.documento.DocumentoDTO;
import io.github.beinlichsimone.vacationtrip.model.Documento;
import io.github.beinlichsimone.vacationtrip.repository.DocumentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public DocumentoDTO create(DocumentoDTO dto) {
        Documento entity = dto.converter(pessoaRepository);
        Documento saved = documentoRepository.save(entity);
        return new DocumentoDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<DocumentoDTO> list(Pageable pageable) {
        return documentoRepository.findAll(pageable).map(DocumentoDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<DocumentoDTO> get(Integer id) {
        return documentoRepository.findById(id).map(DocumentoDetalheDTO -> new DocumentoDTO(DocumentoDetalheDTO));
    }

    @Transactional
    public Optional<DocumentoDTO> update(Integer id, DocumentoAtualizarDTO dto) {
        Optional<Documento> maybe = documentoRepository.findById(id);
        if (maybe.isEmpty()) return Optional.empty();
        Documento entity = dto.atualizar(id, documentoRepository);
        Documento saved = documentoRepository.save(entity);
        return Optional.of(new DocumentoDTO(saved));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!documentoRepository.existsById(id)) return false;
        documentoRepository.deleteById(id);
        return true;
    }
}


