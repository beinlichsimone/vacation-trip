package io.github.beinlichsimone.vacationtrip.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //@NotBlank deve estar na classe de dto para retornar o erro corretamente
    private String nome;

    private String descricao;

    //@DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataIda;

    //@DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataVolta;

    @OneToMany( mappedBy = "viagem", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Pessoa> pessoas;

    @OneToMany( mappedBy = "viagem", fetch = FetchType.EAGER) //sempre precisa do mappedBy quando é OneToMany para indicar qual é o lado inverso ou não dominante da relação.
    @Fetch(FetchMode.SUBSELECT) //não deixa ter mais de um relacionamento do tipo EAGER, essa anotação resolveu o erro.
    private List<Passeio> passeios;

    @OneToMany( mappedBy = "viagem", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Deslocamento> deslocamentos;

    @OneToOne
    private CheckList checkList;

    public Viagem(String nome, String descricao, LocalDate dataIda, LocalDate dataVolta) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataIda = dataIda;
        this.dataVolta = dataVolta;
    }
}
