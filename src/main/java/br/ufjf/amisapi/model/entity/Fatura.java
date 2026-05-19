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
public class Fatura {

    @Id
    // O diagrama pede String para o ID desta classe
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Float valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private String tipo;
    private String status;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "escritorio_id")
    private Escritorio escritorio;
}