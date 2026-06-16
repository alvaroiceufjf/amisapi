package br.ufjf.amisapi.service;

import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Escritorio;
import br.ufjf.amisapi.model.repository.EscritorioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EscritorioService {

    private EscritorioRepository repository;

    public EscritorioService(EscritorioRepository repository) {
        this.repository = repository;
    }

    public List<Escritorio> getEscritorios() {
        return repository.findAll();
    }

    public Optional<Escritorio> getEscritorioById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Escritorio salvar(Escritorio escritorio) {
        validar(escritorio);
        return repository.save(escritorio);
    }

    @Transactional
    public void excluir(Escritorio escritorio) {
        Objects.requireNonNull(escritorio.getId());
        repository.delete(escritorio);
    }

    public void validar(Escritorio escritorio) {
        if (escritorio.getNome() == null || escritorio.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome do escritório inválido");
        }
        if (escritorio.getCnpj() == null || escritorio.getCnpj().trim().equals("")) {
            throw new RegraNegocioException("CNPJ inválido");
        }
    }
}