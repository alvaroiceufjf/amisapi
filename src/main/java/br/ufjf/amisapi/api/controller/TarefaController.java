package br.ufjf.amisapi.api.controller;

import br.ufjf.amisapi.api.dto.TarefaDTO;
import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Processo;
import br.ufjf.amisapi.model.entity.Tarefa;
import br.ufjf.amisapi.model.entity.TipoTarefa;
import br.ufjf.amisapi.model.entity.Usuario;
import br.ufjf.amisapi.service.ProcessoService;
import br.ufjf.amisapi.service.TarefaService;
import br.ufjf.amisapi.service.TipoTarefaService;
import br.ufjf.amisapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tarefas")
@RequiredArgsConstructor
@CrossOrigin
public class TarefaController {

    private final TarefaService service;
    private final TipoTarefaService tipoTarefaService;
    private final UsuarioService usuarioService;
    private final ProcessoService processoService;

    @GetMapping()
    public ResponseEntity get() {
        List<Tarefa> tarefas = service.getTarefas();
        return ResponseEntity.ok(tarefas.stream().map(TarefaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Tarefa> tarefa = service.getTarefaById(id);
        if (!tarefa.isPresent()) {
            return new ResponseEntity("Tarefa não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(TarefaDTO.create(tarefa.get()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody TarefaDTO dto) {
        try {
            Tarefa tarefa = converter(dto);
            tarefa = service.salvar(tarefa);
            return new ResponseEntity(tarefa, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody TarefaDTO dto) {
        if (!service.getTarefaById(id).isPresent()) {
            return new ResponseEntity("Tarefa não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Tarefa tarefa = converter(dto);
            tarefa.setId(id);
            service.salvar(tarefa);
            return ResponseEntity.ok(tarefa);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Tarefa> tarefa = service.getTarefaById(id);
        if (!tarefa.isPresent()) {
            return new ResponseEntity("Tarefa não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(tarefa.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Tarefa converter(TarefaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Tarefa tarefa = modelMapper.map(dto, Tarefa.class);

        if (dto.getTipoTarefaId() != null) {
            Optional<TipoTarefa> tipo = tipoTarefaService.getTipoTarefaById(dto.getTipoTarefaId());
            tarefa.setTipoTarefa(tipo.orElse(null));
        }
        if (dto.getResponsavelId() != null) {
            Optional<Usuario> usuario = usuarioService.getUsuarioById(dto.getResponsavelId());
            tarefa.setResponsavel(usuario.orElse(null));
        }
        if (dto.getProcessoId() != null) {
            Optional<Processo> processo = processoService.getProcessoById(dto.getProcessoId());
            tarefa.setProcesso(processo.orElse(null));
        }
        return tarefa;
    }
}