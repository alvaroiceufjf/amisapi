package br.ufjf.amisapi.api.controller;

import br.ufjf.amisapi.api.dto.EscritorioDTO;
import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Escritorio;
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
@RequestMapping("/api/v1/escritorios")
@RequiredArgsConstructor
@CrossOrigin
public class EscritorioController {

    private final EscritorioService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Escritorio> escritorios = service.getEscritorios();
        return ResponseEntity.ok(escritorios.stream().map(EscritorioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Escritorio> escritorio = service.getEscritorioById(id);
        if (!escritorio.isPresent()) {
            return new ResponseEntity("Escritório não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(EscritorioDTO.create(escritorio.get()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody EscritorioDTO dto) {
        try {
            Escritorio escritorio = converter(dto);
            escritorio = service.salvar(escritorio);
            return new ResponseEntity(escritorio, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody EscritorioDTO dto) {
        if (!service.getEscritorioById(id).isPresent()) {
            return new ResponseEntity("Escritório não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Escritorio escritorio = converter(dto);
            escritorio.setId(id);
            service.salvar(escritorio);
            return ResponseEntity.ok(escritorio);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Escritorio> escritorio = service.getEscritorioById(id);
        if (!escritorio.isPresent()) {
            return new ResponseEntity("Escritório não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(escritorio.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Escritorio converter(EscritorioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Escritorio.class);
    }
}