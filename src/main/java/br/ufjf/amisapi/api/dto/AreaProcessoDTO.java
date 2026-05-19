package br.ufjf.amisapi.api.dto;

import br.ufjf.amisapi.model.entity.AreaProcesso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaProcessoDTO {
    private String id;
    private String nome;

    public static AreaProcessoDTO create(AreaProcesso areaProcesso) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(areaProcesso, AreaProcessoDTO.class);
    }
}
