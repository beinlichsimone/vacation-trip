package io.github.beinlichsimone.vacationtrip.config.validacao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErroFormularioDTO
{
    private String campo;
    private String erro;
}
