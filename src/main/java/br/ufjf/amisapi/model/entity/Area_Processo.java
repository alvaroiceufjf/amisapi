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
public class Area_Processo {

    @Id
    // O diagrama pede String para o ID desta classe
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;
}
