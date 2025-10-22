package io.github.beinlichsimone.vacationtrip.services;

import io.github.beinlichsimone.vacationtrip.client.VooClient;
import io.github.beinlichsimone.vacationtrip.dto.InfoVooDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoosService {

    private static final Logger LOG = LoggerFactory.getLogger(VoosService.class);

    @Autowired
    private VooClient vooClient;

    public void consulta(String voo) {

        LOG.info("Consultando Status do Voo");
        InfoVooDTO infoVooDTO = vooClient.consultaVoos(voo);

        System.out.println(infoVooDTO.getVoo());
    }
}
