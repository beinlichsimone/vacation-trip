package io.github.beinlichsimone.vacationtrip.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.beinlichsimone.vacationtrip.dto.InfoVooDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqService {

    @Autowired
    RabbitTemplate rabbitmqTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void enviaMensagem(String nomeFila, InfoVooDTO mensagem) throws JsonProcessingException {
        this.rabbitmqTemplate.convertAndSend(nomeFila, objectMapper.writeValueAsString(mensagem));
        System.out.println("Enviando mensagem para a fila: " + nomeFila);
        System.out.println("Mensagem: " + mensagem);
    }
}
