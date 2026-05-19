package br.ufjf.amisapi.api.dto;

import br.ufjf.amisapi.model.entity.TipoTarefa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoTarefaDTO {
    private String id;
    private String nome;

    public static TipoTarefaDTO create(TipoTarefa tipoTarefa) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(tipoTarefa, TipoTarefaDTO.class);
    }
}
