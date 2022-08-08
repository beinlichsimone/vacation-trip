package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.repository.CheckListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("checkList")
public class CheckListController {

    @Autowired
    private CheckListRepository checkListRepository;
}
