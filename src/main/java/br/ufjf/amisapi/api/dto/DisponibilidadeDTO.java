package br.ufjf.amisapi.api.dto;

import br.ufjf.amisapi.model.entity.Disponibilidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadeDTO {
    private Long id;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Long usuarioId;

    public static DisponibilidadeDTO create(Disponibilidade disponibilidade) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(disponibilidade, DisponibilidadeDTO.class);
    }
}
