package io.github.beinlichsimone.vacationtrip.repository;

import io.github.beinlichsimone.vacationtrip.model.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface ViagemRepository extends JpaRepository<Viagem, Integer> {

    //@Cacheable("books")
    List<Viagem> findAll();

    Viagem findByNome(String nomeViagem);

    @EntityGraph(attributePaths = {"pessoas"})
    Optional<Viagem> findWithDetalhesById(Integer id);
}
