package br.ufjf.amisapi.api.controller;

import br.ufjf.amisapi.api.dto.AreaProcessoDTO;
import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.AreaProcesso;
import br.ufjf.amisapi.service.AreaProcessoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/areas-processo")
@RequiredArgsConstructor
@CrossOrigin
public class AreaProcessoController {

    private final AreaProcessoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<AreaProcesso> areas = service.getAreasProcesso();
        return ResponseEntity.ok(areas.stream().map(AreaProcessoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") String id) { // Note o String aqui
        Optional<AreaProcesso> area = service.getAreaProcessoById(id);
        if (!area.isPresent()) {
            return new ResponseEntity("Área de processo não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(AreaProcessoDTO.create(area.get()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody AreaProcessoDTO dto) {
        try {
            AreaProcesso area = converter(dto);
            area = service.salvar(area);
            return new ResponseEntity(area, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") String id, @RequestBody AreaProcessoDTO dto) { // Note o String aqui
        if (!service.getAreaProcessoById(id).isPresent()) {
            return new ResponseEntity("Área de processo não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            AreaProcesso area = converter(dto);
            area.setId(id);
            service.salvar(area);
            return ResponseEntity.ok(area);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") String id) { // Note o String aqui
        Optional<AreaProcesso> area = service.getAreaProcessoById(id);
        if (!area.isPresent()) {
            return new ResponseEntity("Área de processo não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(area.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public AreaProcesso converter(AreaProcessoDTO dto) {
        return new ModelMapper().map(dto, AreaProcesso.class);
    }
}