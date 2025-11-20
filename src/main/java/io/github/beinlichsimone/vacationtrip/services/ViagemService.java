package io.github.beinlichsimone.vacationtrip.services;

import io.github.beinlichsimone.vacationtrip.dto.viagem.ViagemDTO;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    public Viagem salvar(Viagem viagem) {
        return viagemRepository.save(viagem);
    }

    public Optional<Viagem> encontrarPeloId(Integer id){
        return viagemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Viagem> encontrarDetalhesPeloId(Integer id){
        return viagemRepository.findWithDetalhesById(id);
    }

    public void deletarPeloId(Integer id){
        viagemRepository.deleteById(id);
    }

    public Viagem atualizar (Integer id, ViagemDTO viagemDTO){
        ModelMapper modelMapper = new ModelMapper();
        Viagem viagem = modelMapper.map(viagemDTO, Viagem.class);
        viagem.setId(id);
        viagemRepository.save(viagem);

        return viagem;
    }

    public Page<Viagem> findAllPaginacao(Pageable paginacao){
        Page<Viagem> viagens = viagemRepository.findAll(paginacao);
        return viagens;
    }
}
