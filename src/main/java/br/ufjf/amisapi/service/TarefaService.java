package br.ufjf.amisapi.service;

import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Tarefa;
import br.ufjf.amisapi.model.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TarefaService {

    private TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    public List<Tarefa> getTarefas() {
        return repository.findAll();
    }

    public Optional<Tarefa> getTarefaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Tarefa salvar(Tarefa tarefa) {
        validar(tarefa);
        return repository.save(tarefa);
    }

    @Transactional
    public void excluir(Tarefa tarefa) {
        Objects.requireNonNull(tarefa.getId());
        repository.delete(tarefa);
    }

    public void validar(Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().equals("")) {
            throw new RegraNegocioException("Título da tarefa inválido");
        }
        if (tarefa.getResponsavel() == null || tarefa.getResponsavel().getId() == null) {
            throw new RegraNegocioException("A tarefa deve ter um advogado responsável");
        }
    }
}