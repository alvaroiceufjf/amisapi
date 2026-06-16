package br.ufjf.amisapi.api.controller;

import br.ufjf.amisapi.api.dto.FaturaDTO;
import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Cliente;
import br.ufjf.amisapi.model.entity.Escritorio;
import br.ufjf.amisapi.model.entity.Fatura;
import br.ufjf.amisapi.service.ClienteService;
import br.ufjf.amisapi.service.EscritorioService;
import br.ufjf.amisapi.service.FaturaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/faturas")
@RequiredArgsConstructor
@CrossOrigin
public class FaturaController {

    private final FaturaService service;
    private final ClienteService clienteService;
    private final EscritorioService escritorioService;

    @GetMapping()
    public ResponseEntity get() {
        List<Fatura> faturas = service.getFaturas();
        return ResponseEntity.ok(faturas.stream().map(FaturaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") String id) {
        Optional<Fatura> fatura = service.getFaturaById(id);
        if (!fatura.isPresent()) {
            return new ResponseEntity("Fatura não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(FaturaDTO.create(fatura.get()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody FaturaDTO dto) {
        try {
            Fatura fatura = converter(dto);
            fatura = service.salvar(fatura);
            return new ResponseEntity(fatura, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") String id, @RequestBody FaturaDTO dto) {
        if (!service.getFaturaById(id).isPresent()) {
            return new ResponseEntity("Fatura não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Fatura fatura = converter(dto);
            fatura.setId(id);
            service.salvar(fatura);
            return ResponseEntity.ok(fatura);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") String id) {
        Optional<Fatura> fatura = service.getFaturaById(id);
        if (!fatura.isPresent()) {
            return new ResponseEntity("Fatura não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(fatura.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Fatura converter(FaturaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Fatura fatura = modelMapper.map(dto, Fatura.class);

        if (dto.getClienteId() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getClienteId());
            fatura.setCliente(cliente.orElse(null));
        }
        if (dto.getEscritorioId() != null) {
            Optional<Escritorio> escritorio = escritorioService.getEscritorioById(dto.getEscritorioId());
            fatura.setEscritorio(escritorio.orElse(null));
        }
        return fatura;
    }
}