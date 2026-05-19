package br.ufjf.amisapi.api.dto;

import br.ufjf.amisapi.model.entity.Fatura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaturaDTO {
    private String id;
    private Float valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private String tipo;
    private String status;
    private Long clienteId;
    private Long escritorioId;

    public static FaturaDTO create(Fatura fatura) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(fatura, FaturaDTO.class);
    }
}
