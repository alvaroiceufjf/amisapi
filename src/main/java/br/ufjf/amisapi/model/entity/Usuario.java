package br.ufjf.amisapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private String email;
    private String oab;

    // Representa a chave estrangeira Escritorio_ID do diagrama
    @ManyToOne
    @JoinColumn(name = "escritorio_id")
    private Escritorio escritorio;
}
