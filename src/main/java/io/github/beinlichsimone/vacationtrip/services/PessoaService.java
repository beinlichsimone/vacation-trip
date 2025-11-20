package io.github.beinlichsimone.vacationtrip.services;

import io.github.beinlichsimone.vacationtrip.dto.pessoa.PessoaDTO;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import io.github.beinlichsimone.vacationtrip.model.Viagem;
import io.github.beinlichsimone.vacationtrip.repository.PessoaRepository;
import io.github.beinlichsimone.vacationtrip.repository.ViagemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public PessoaDTO create(PessoaDTO dto) {
        Pessoa pessoa = modelMapper.map(dto, Pessoa.class);
        if (dto.getIdViagem() != null) {
            Optional<Viagem> viagem = viagemRepository.findById(dto.getIdViagem());
            pessoa.setViagem(viagem.orElse(null));
        } else {
            pessoa.setViagem(null);
        }
        Pessoa saved = pessoaRepository.save(pessoa);
        PessoaDTO out = modelMapper.map(saved, PessoaDTO.class);
        if (saved.getViagem() != null) {
            out.setIdViagem(saved.getViagem().getId());
        }
        out.setId(String.valueOf(saved.getId()));
        return out;
    }

    @Transactional(readOnly = true)
    public Page<PessoaDTO> list(Pageable pageable) {
        return pessoaRepository.findAll(pageable).map(p -> {
            PessoaDTO dto = modelMapper.map(p, PessoaDTO.class);
            dto.setId(String.valueOf(p.getId()));
            if (p.getViagem() != null) {
                dto.setIdViagem(p.getViagem().getId());
            }
            return dto;
        });
    }

    @Transactional(readOnly = true)
    public Optional<PessoaDTO> get(Integer id) {
        return pessoaRepository.findById(id).map(p -> {
            PessoaDTO dto = modelMapper.map(p, PessoaDTO.class);
            dto.setId(String.valueOf(p.getId()));
            if (p.getViagem() != null) {
                dto.setIdViagem(p.getViagem().getId());
            }
            return dto;
        });
    }

    @Transactional
    public Optional<PessoaDTO> update(Integer id, PessoaDTO dto) {
        Optional<Pessoa> maybe = pessoaRepository.findById(id);
        if (maybe.isEmpty()) return Optional.empty();
        Pessoa pessoa = maybe.get();
        pessoa.setNome(dto.getNome());
        pessoa.setCpf(dto.getCpf());
        pessoa.setEmail(dto.getEmail());
        if (dto.getIdViagem() != null) {
            Optional<Viagem> viagem = viagemRepository.findById(dto.getIdViagem());
            pessoa.setViagem(viagem.orElse(null));
        } else {
            pessoa.setViagem(null);
        }
        Pessoa saved = pessoaRepository.save(pessoa);
        PessoaDTO out = modelMapper.map(saved, PessoaDTO.class);
        out.setId(String.valueOf(saved.getId()));
        if (saved.getViagem() != null) {
            out.setIdViagem(saved.getViagem().getId());
        }
        return Optional.of(out);
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!pessoaRepository.existsById(id)) {
            return false;
        }
        pessoaRepository.deleteById(id);
        return true;
    }
}


