package io.github.beinlichsimone.vacationtrip.repository;

import io.github.beinlichsimone.vacationtrip.model.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckListRepository  extends JpaRepository<CheckList, Integer> {
}
