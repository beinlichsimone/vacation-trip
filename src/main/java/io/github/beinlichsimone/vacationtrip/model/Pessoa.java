package io.github.beinlichsimone.vacationtrip.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    private String cpf;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dtNascimento;

    private String endereco;
    private Integer telefone;

    //@Email
    private String email;

    @OneToMany( mappedBy = "pessoa", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Documento> documentos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viagem_id")
    private Viagem viagem;

}
