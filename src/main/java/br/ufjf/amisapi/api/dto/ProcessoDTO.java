package br.ufjf.amisapi.api.dto;

import br.ufjf.amisapi.model.entity.Processo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoDTO {
    private Long id;
    private String numeroCnj;
    private String tribunal;
    private String status;
    private String areaProcessoId;
    private Long clienteId;

    public static ProcessoDTO create(Processo processo) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(processo, ProcessoDTO.class);
    }
}
