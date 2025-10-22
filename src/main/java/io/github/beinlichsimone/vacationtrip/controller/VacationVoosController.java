package io.github.beinlichsimone.vacationtrip.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.beinlichsimone.vacationtrip.dto.InfoVooDTO;
import io.github.beinlichsimone.vacationtrip.services.RabbitmqService;
import io.github.beinlichsimone.vacationtrip.services.VoosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voos")
public class VacationVoosController {

    @Autowired
    private VoosService voosService;

    @Autowired
    private RabbitmqService rabbitmqService;

    @RequestMapping("/{voo}")
    public void consultaVoos(@PathVariable String voo){
        InfoVooDTO infoVooDTO = new InfoVooDTO();
        infoVooDTO.setVoo(voo);
        try {
            this.rabbitmqService.enviaMensagem("ms.voo", infoVooDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //voosService.consulta(voo);
    }

}
