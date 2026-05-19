package br.ufjf.amisapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Float valor;
    private LocalDate dataAssinatura;
    private String objeto;

    @ManyToOne
    @JoinColumn(name = "escritorio_id")
    private Escritorio escritorio;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
