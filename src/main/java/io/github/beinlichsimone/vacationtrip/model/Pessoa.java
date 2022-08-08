package io.github.beinlichsimone.vacationtrip.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //@NotBlank deve estar na classe de dto para retornar o erro corretamente
    private String nome;

    //@CPF
    private Integer cpf;

    //@DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dtNascimento;

    private String endereco;

    private Integer telefone;

    //@Email
    private String email;

    @OneToMany( mappedBy = "pessoa", fetch = FetchType.EAGER)
    private List<Documento> documentos;

    @ManyToOne
    @JoinColumn(name = "viagem_id")
    private Viagem viagem;

    public Pessoa(Integer id, String nome, Integer cpf, Viagem viagem) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.viagem = viagem;
    }

}
