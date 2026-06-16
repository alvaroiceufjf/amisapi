package br.ufjf.amisapi.service;

import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.TipoTarefa;
import br.ufjf.amisapi.model.repository.TipoTarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TipoTarefaService {

    private TipoTarefaRepository repository;

    public TipoTarefaService(TipoTarefaRepository repository) {
        this.repository = repository;
    }

    public List<TipoTarefa> getTiposTarefa() {
        return repository.findAll();
    }

    // Como o ID definido no DTO/Entity anterior foi String (UUID), recebemos String aqui
    public Optional<TipoTarefa> getTipoTarefaById(String id) {
        return repository.findById(id);
    }

    @Transactional
    public TipoTarefa salvar(TipoTarefa tipoTarefa) {
        validar(tipoTarefa);
        return repository.save(tipoTarefa);
    }

    @Transactional
    public void excluir(TipoTarefa tipoTarefa) {
        Objects.requireNonNull(tipoTarefa.getId());
        repository.delete(tipoTarefa);
    }

    public void validar(TipoTarefa tipoTarefa) {
        if (tipoTarefa.getNome() == null || tipoTarefa.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome do Tipo de Tarefa inválido");
        }
    }
}