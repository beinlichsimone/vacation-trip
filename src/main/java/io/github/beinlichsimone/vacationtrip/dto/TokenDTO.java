package io.github.beinlichsimone.vacationtrip.dto;

import lombok.Data;

@Data
public class TokenDTO {

    private String token;
    private String tipo;

    public TokenDTO(String token, String tipo) {
        this.token = token;
        this.tipo = tipo;

    }
}
