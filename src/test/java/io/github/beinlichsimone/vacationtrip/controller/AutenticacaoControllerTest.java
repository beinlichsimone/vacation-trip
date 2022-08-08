package io.github.beinlichsimone.vacationtrip.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
//@WebMvcTest
@AutoConfigureMockMvc
@SpringBootTest
public class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void devedevolver400CasoDadosDeAutenticacaoEstejamIncorretos() throws Exception {
        URI uri = new URI("/auth");
        String json = "{\"email\":\"simone@gmail.com\",\"senha\":\"12345\"}";

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .is(400)); // 400  bad request, deveria ser 403 que significa não ter autenticação

    }
}
