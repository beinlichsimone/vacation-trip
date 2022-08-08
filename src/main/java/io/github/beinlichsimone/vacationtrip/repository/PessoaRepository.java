package io.github.beinlichsimone.vacationtrip.repository;

import io.github.beinlichsimone.vacationtrip.model.Passeio;
import io.github.beinlichsimone.vacationtrip.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

}
