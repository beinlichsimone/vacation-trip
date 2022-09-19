package io.github.beinlichsimone.vacationtrip.services;

import io.github.beinlichsimone.vacationtrip.dto.viagem.ViagemAtualizaDTO;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    public void salvar(Viagem viagem) {
        viagemRepository.save(viagem);
    }

    public Optional<Viagem> encontrarPeloId(Integer id){
        Optional<Viagem> viagem = viagemRepository.findById(id);
        return viagem;
    }

    public void deletarPeloId(Integer id){
        viagemRepository.deleteById(id);
    }

    public Viagem atualizar (Integer id, ViagemAtualizaDTO viagemDTO){
        Optional<Viagem> viagemOp = viagemRepository.findById(id);
        Viagem viagem = viagemOp.get();

        viagem.setNome(viagemDTO.getNome());
        viagem.setDescricao(viagemDTO.getDescricao());
        viagemRepository.save(viagem);

        return viagem;
    }

    public Page<Viagem> encontrarTodas(Pageable paginacao){
        Page<Viagem> viagens = viagemRepository.findAll(paginacao);
        return viagens;
    }
}
