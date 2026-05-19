package br.ufjf.amisapi.api.dto;

import br.ufjf.amisapi.model.entity.Tarefa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaDTO {
    private Long id;
    private String titulo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String observacoes;
    private String tipoTarefaId;
    private Long responsavelId;
    private Long processoId;

    public static TarefaDTO create(Tarefa tarefa) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(tarefa, TarefaDTO.class);
    }
}
