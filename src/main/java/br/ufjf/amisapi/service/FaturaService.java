package br.ufjf.amisapi.service;

import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Fatura;
import br.ufjf.amisapi.model.repository.FaturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FaturaService {

    private FaturaRepository repository;

    public FaturaService(FaturaRepository repository) {
        this.repository = repository;
    }

    public List<Fatura> getFaturas() {
        return repository.findAll();
    }

    // Atenção: O ID aqui é String
    public Optional<Fatura> getFaturaById(String id) {
        return repository.findById(id);
    }

    @Transactional
    public Fatura salvar(Fatura fatura) {
        validar(fatura);
        return repository.save(fatura);
    }

    @Transactional
    public void excluir(Fatura fatura) {
        Objects.requireNonNull(fatura.getId());
        repository.delete(fatura);
    }

    public void validar(Fatura fatura) {
        if (fatura.getValor() == null || fatura.getValor() <= 0) {
            throw new RegraNegocioException("Valor da fatura inválido");
        }
        if (fatura.getTipo() == null || fatura.getTipo().trim().equals("")) {
            throw new RegraNegocioException("Tipo da fatura inválido");
        }
        if (fatura.getStatus() == null || fatura.getStatus().trim().equals("")) {
            throw new RegraNegocioException("Status da fatura inválido");
        }
        if (fatura.getCliente() == null || fatura.getCliente().getId() == null) {
            throw new RegraNegocioException("A fatura deve estar vinculada a um cliente");
        }
    }
}