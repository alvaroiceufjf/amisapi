package br.ufjf.amisapi.api.controller;

import br.ufjf.amisapi.api.dto.TipoTarefaDTO;
import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.TipoTarefa;
import br.ufjf.amisapi.service.TipoTarefaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tipos-tarefa")
@RequiredArgsConstructor
@CrossOrigin
public class TipoTarefaController {

    private final TipoTarefaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<TipoTarefa> tipos = service.getTiposTarefa();
        return ResponseEntity.ok(tipos.stream().map(TipoTarefaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") String id) {
        Optional<TipoTarefa> tipo = service.getTipoTarefaById(id);
        if (!tipo.isPresent()) {
            return new ResponseEntity("Tipo de tarefa não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(TipoTarefaDTO.create(tipo.get()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody TipoTarefaDTO dto) {
        try {
            TipoTarefa tipo = converter(dto);
            tipo = service.salvar(tipo);
            return new ResponseEntity(tipo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") String id, @RequestBody TipoTarefaDTO dto) {
        if (!service.getTipoTarefaById(id).isPresent()) {
            return new ResponseEntity("Tipo de tarefa não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            TipoTarefa tipo = converter(dto);
            tipo.setId(id);
            service.salvar(tipo);
            return ResponseEntity.ok(tipo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") String id) {
        Optional<TipoTarefa> tipo = service.getTipoTarefaById(id);
        if (!tipo.isPresent()) {
            return new ResponseEntity("Tipo de tarefa não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(tipo.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public TipoTarefa converter(TipoTarefaDTO dto) {
        return new ModelMapper().map(dto, TipoTarefa.class);
    }
}