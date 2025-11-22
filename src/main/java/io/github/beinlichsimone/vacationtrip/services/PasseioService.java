package io.github.beinlichsimone.vacationtrip.services;

import io.github.beinlichsimone.vacationtrip.dto.passeio.PasseioAtualizarDTO;
import io.github.beinlichsimone.vacationtrip.dto.passeio.PasseioDTO;
import io.github.beinlichsimone.vacationtrip.model.Passeio;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.PasseioRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PasseioService {

    @Autowired
    private PasseioRepository passeioRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @Transactional
    public PasseioDTO create(PasseioDTO dto) {
        Passeio entity = new Passeio();
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setObservacao(dto.getObservacao());
        entity.setLinks(dto.getLinks());
        entity.setDataPasseio(dto.getDataPasseio());
        if (dto.getIdViagem() != null) {
            Optional<Viagem> viagem = viagemRepository.findById(dto.getIdViagem());
            entity.setViagem(viagem.orElse(null));
        }
        Passeio saved = passeioRepository.save(entity);
        return new PasseioDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<PasseioDTO> list(Pageable pageable) {
        return passeioRepository.findAll(pageable).map(PasseioDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<PasseioDTO> get(Integer id) {
        return passeioRepository.findById(id).map(PasseioDetalheDTO -> new PasseioDTO(PasseioDetalheDTO));
    }

    @Transactional
    public Optional<PasseioDTO> update(Integer id, PasseioAtualizarDTO dto) {
        Optional<Passeio> maybe = passeioRepository.findById(id);
        if (maybe.isEmpty()) return Optional.empty();
        Passeio entity = dto.atualizar(id, passeioRepository);
        Passeio saved = passeioRepository.save(entity);
        return Optional.of(new PasseioDTO(saved));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!passeioRepository.existsById(id)) return false;
        passeioRepository.deleteById(id);
        return true;
    }
}


