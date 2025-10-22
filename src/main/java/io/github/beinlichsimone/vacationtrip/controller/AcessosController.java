package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.interceptor.InterceptadorDeAcessos;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("acessos")
@RestController
public class AcessosController {

    @GetMapping
    public List<InterceptadorDeAcessos.Acesso> getAcessos(){
        return InterceptadorDeAcessos.acessos;
    }
}
