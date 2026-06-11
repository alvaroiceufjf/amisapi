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
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String numeroCnj;
    private String tribunal;
    private String status;

    @ManyToOne
    @JoinColumn(name = "area_processo_id")
    private AreaProcesso areaProcesso;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}