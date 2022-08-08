package io.github.beinlichsimone.vacationtrip.repository;

import io.github.beinlichsimone.vacationtrip.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository  extends JpaRepository<Documento, Integer> {
}
