package br.ufjf.amisapi.api.controller;

import br.ufjf.amisapi.api.dto.ContratoDTO;
import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Cliente;
import br.ufjf.amisapi.model.entity.Contrato;
import br.ufjf.amisapi.model.entity.Escritorio;
import br.ufjf.amisapi.service.ClienteService;
import br.ufjf.amisapi.service.ContratoService;
import br.ufjf.amisapi.service.EscritorioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/contratos")
@RequiredArgsConstructor
@CrossOrigin
public class ContratoController {

    private final ContratoService service;
    private final ClienteService clienteService;
    private final EscritorioService escritorioService;

    @GetMapping()
    public ResponseEntity get() {
        List<Contrato> contratos = service.getContratos();
        return ResponseEntity.ok(contratos.stream().map(ContratoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Contrato> contrato = service.getContratoById(id);
        if (!contrato.isPresent()) {
            return new ResponseEntity("Contrato não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ContratoDTO.create(contrato.get()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ContratoDTO dto) {
        try {
            Contrato contrato = converter(dto);
            contrato = service.salvar(contrato);
            return new ResponseEntity(contrato, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ContratoDTO dto) {
        if (!service.getContratoById(id).isPresent()) {
            return new ResponseEntity("Contrato não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Contrato contrato = converter(dto);
            contrato.setId(id);
            service.salvar(contrato);
            return ResponseEntity.ok(contrato);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Contrato> contrato = service.getContratoById(id);
        if (!contrato.isPresent()) {
            return new ResponseEntity("Contrato não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(contrato.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Contrato converter(ContratoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Contrato contrato = modelMapper.map(dto, Contrato.class);

        if (dto.getClienteId() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getClienteId());
            contrato.setCliente(cliente.orElse(null));
        }
        if (dto.getEscritorioId() != null) {
            Optional<Escritorio> escritorio = escritorioService.getEscritorioById(dto.getEscritorioId());
            contrato.setEscritorio(escritorio.orElse(null));
        }
        return contrato;
    }
}