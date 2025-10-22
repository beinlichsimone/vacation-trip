package io.github.beinlichsimone.vacationtrip.repository;

import io.github.beinlichsimone.vacationtrip.model.Viagem;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ViagemRepository extends JpaRepository<Viagem, Integer> {

    //@Cacheable("books")
    List<Viagem> findAll();

    Viagem findByNome(String nomeViagem);
}
