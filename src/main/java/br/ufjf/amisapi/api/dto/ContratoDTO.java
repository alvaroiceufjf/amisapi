package br.ufjf.amisapi.api.dto;

import br.ufjf.amisapi.model.entity.Contrato;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoDTO {
    private Long id;
    private Float valor;
    private LocalDate dataAssinatura;
    private String objeto;
    private Long escritorioId;
    private Long clienteId;

    public static ContratoDTO create(Contrato contrato) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(contrato, ContratoDTO.class);
    }
}