package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.repository.DocumentoRepository;
import io.github.beinlichsimone.vacationtrip.repository.ImagemDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("imagemDocumento")
public class ImagemDocumentoController {

    @Autowired
    private ImagemDocumentoRepository imagemDocumentoRepository;
}
