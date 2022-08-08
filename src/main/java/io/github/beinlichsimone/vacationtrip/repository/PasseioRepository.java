package io.github.beinlichsimone.vacationtrip.repository;

import io.github.beinlichsimone.vacationtrip.model.CheckList;
import io.github.beinlichsimone.vacationtrip.model.Passeio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasseioRepository extends JpaRepository<Passeio, Integer> {
}
