package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.services.VoosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voos")
public class VoosController {

    @Autowired
    private VoosService voosService;

    @RequestMapping("/{voo}")
    public void consultaVoos(@PathVariable String voo){
        voosService.consulta(voo);
    }

}
