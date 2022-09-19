package io.github.beinlichsimone.vacationtrip.client;

import io.github.beinlichsimone.vacationtrip.dto.InfoVooDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("voo")
public interface VooClient {

    @RequestMapping("/consulta/{voo}")
    InfoVooDTO consultaVoos(@PathVariable String voo);
}
