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
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titulo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "tipo_tarefa_id")
    private TipoTarefa tipoTarefa;


    @ManyToOne
    @JoinColumn(name = "usuario_responsavel_id")
    private Usuario responsavel;

    @ManyToOne
    @JoinColumn(name = "processo_id")
    private Processo processo;
}
