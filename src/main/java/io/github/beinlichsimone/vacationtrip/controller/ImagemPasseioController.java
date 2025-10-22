package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.repository.ImagemDeslocamentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.ImagemPasseioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("imagemPasseio")
public class ImagemPasseioController {

    @Autowired
    private ImagemPasseioRepository imagemPasseioRepository;
}
