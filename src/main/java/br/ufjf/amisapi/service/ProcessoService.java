package br.ufjf.amisapi.service;

import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Processo;
import br.ufjf.amisapi.model.repository.ProcessoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProcessoService {

    private ProcessoRepository repository;

    public ProcessoService(ProcessoRepository repository) {
        this.repository = repository;
    }

    public List<Processo> getProcessos() {
        return repository.findAll();
    }

    public Optional<Processo> getProcessoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Processo salvar(Processo processo) {
        validar(processo);
        return repository.save(processo);
    }

    @Transactional
    public void excluir(Processo processo) {
        Objects.requireNonNull(processo.getId());
        repository.delete(processo);
    }

    public void validar(Processo processo) {
        if (processo.getNumeroCnj() == null || processo.getNumeroCnj().trim().equals("")) {
            throw new RegraNegocioException("Número CNJ do processo inválido");
        }
        if (processo.getCliente() == null || processo.getCliente().getId() == null) {
            throw new RegraNegocioException("O processo deve estar vinculado a um cliente");
        }
    }
}