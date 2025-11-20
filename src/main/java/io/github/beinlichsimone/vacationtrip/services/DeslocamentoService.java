package io.github.beinlichsimone.vacationtrip.services;

import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.deslocamento.DeslocamentoDTO;
import io.github.beinlichsimone.vacationtrip.model.Deslocamento;
import io.github.beinlichsimone.vacationtrip.model.Passeio;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.DeslocamentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.PasseioRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DeslocamentoService {

    @Autowired
    private DeslocamentoRepository deslocamentoRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private PasseioRepository passeioRepository;

    @Transactional
    public DeslocamentoDTO create(DeslocamentoDTO dto) {
        Deslocamento entity = new Deslocamento();
        entity.setNome(dto.getNome());
        entity.setLocal(dto.getLocal());
        entity.setDescricao(dto.getDescricao());
        entity.setValor(dto.getValor());
        entity.setLink(dto.getLink());
        entity.setVeiculo(dto.getVeiculo());
        entity.setIda(dto.getIda());
        entity.setVolta(dto.getVolta());
        if (dto.getIdViagem() != null) {
            Optional<Viagem> viagem = viagemRepository.findById(dto.getIdViagem());
            entity.setViagem(viagem.orElse(null));
        }
        if (dto.getIdPasseio() != null) {
            Optional<Passeio> passeio = passeioRepository.findById(dto.getIdPasseio());
            entity.setPasseio(passeio.orElse(null));
        }
        Deslocamento saved = deslocamentoRepository.save(entity);
        return new DeslocamentoDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<DeslocamentoDTO> list(Pageable pageable) {
        return deslocamentoRepository.findAll(pageable).map(DeslocamentoDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<DeslocamentoDTO> get(Integer id) {
        return deslocamentoRepository.findById(id).map(DeslocamentoDTO::new);
    }

    @Transactional
    public Optional<DeslocamentoDTO> update(Integer id, DeslocamentoAtualizarDTO dto) {
        Optional<Deslocamento> maybe = deslocamentoRepository.findById(id);
        if (maybe.isEmpty()) return Optional.empty();
        Deslocamento entity = dto.atualizar(id, deslocamentoRepository);
        Deslocamento saved = deslocamentoRepository.save(entity);
        return Optional.of(new DeslocamentoDTO(saved));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!deslocamentoRepository.existsById(id)) return false;
        deslocamentoRepository.deleteById(id);
        return true;
    }
}


