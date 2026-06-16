package br.ufjf.amisapi.api.controller;

import br.ufjf.amisapi.api.dto.ProcessoDTO;
import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.AreaProcesso;
import br.ufjf.amisapi.model.entity.Cliente;
import br.ufjf.amisapi.model.entity.Processo;
import br.ufjf.amisapi.service.AreaProcessoService;
import br.ufjf.amisapi.service.ClienteService;
import br.ufjf.amisapi.service.ProcessoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/processos")
@RequiredArgsConstructor
@CrossOrigin
public class ProcessoController {

    private final ProcessoService service;
    private final ClienteService clienteService;
    private final AreaProcessoService areaProcessoService;

    @GetMapping()
    public ResponseEntity get() {
        List<Processo> processos = service.getProcessos();
        return ResponseEntity.ok(processos.stream().map(ProcessoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Processo> processo = service.getProcessoById(id);
        if (!processo.isPresent()) {
            return new ResponseEntity("Processo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ProcessoDTO.create(processo.get()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ProcessoDTO dto) {
        try {
            Processo processo = converter(dto);
            processo = service.salvar(processo);
            return new ResponseEntity(processo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ProcessoDTO dto) {
        if (!service.getProcessoById(id).isPresent()) {
            return new ResponseEntity("Processo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Processo processo = converter(dto);
            processo.setId(id);
            service.salvar(processo);
            return ResponseEntity.ok(processo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Processo> processo = service.getProcessoById(id);
        if (!processo.isPresent()) {
            return new ResponseEntity("Processo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(processo.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Processo converter(ProcessoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Processo processo = modelMapper.map(dto, Processo.class);

        if (dto.getClienteId() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getClienteId());
            if (!cliente.isPresent()) {
                processo.setCliente(null);
            } else {
                processo.setCliente(cliente.get());
            }
        }

        if (dto.getAreaProcessoId() != null) {
            Optional<AreaProcesso> area = areaProcessoService.getAreaProcessoById(dto.getAreaProcessoId());
            if (!area.isPresent()) {
                processo.setAreaProcesso(null);
            } else {
                processo.setAreaProcesso(area.get());
            }
        }
        return processo;
    }
}