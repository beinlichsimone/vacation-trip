package io.github.beinlichsimone.vacationtrip.repository;

import io.github.beinlichsimone.vacationtrip.model.Viagem;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //se não tiver essa anotação ele vai tentar rodar usando o BD H2. Desta forma ele vai considerar o Postgres
// @ActiveProfiles("test") para o caso de eu querer rodar a classe em um outro profile (por exemplo, quando eu usar um BD diferente pra teste, eu posso ter um outro application.propertier)
public class ViagemRespositoryTeste {

    @Autowired
    private ViagemRepository viagemRepository;

    @Test
    public void deveCarregarUmaViagemAoBuscarPeloNome (){
        String nomeViagem = "férias 2025";
        Viagem viagem = viagemRepository.findByNome(nomeViagem);
        Assert.assertNotNull(viagem);
        Assert.assertEquals(nomeViagem, viagem.getNome());
    }
}
