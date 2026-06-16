package br.ufjf.amisapi.service;

import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Contrato;
import br.ufjf.amisapi.model.repository.ContratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContratoService {

    private ContratoRepository repository;

    public ContratoService(ContratoRepository repository) {
        this.repository = repository;
    }

    public List<Contrato> getContratos() {
        return repository.findAll();
    }

    public Optional<Contrato> getContratoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Contrato salvar(Contrato contrato) {
        validar(contrato);
        return repository.save(contrato);
    }

    @Transactional
    public void excluir(Contrato contrato) {
        Objects.requireNonNull(contrato.getId());
        repository.delete(contrato);
    }

    public void validar(Contrato contrato) {
        if (contrato.getValor() == null || contrato.getValor() <= 0) {
            throw new RegraNegocioException("Valor do contrato inválido");
        }
        if (contrato.getCliente() == null || contrato.getCliente().getId() == null) {
            throw new RegraNegocioException("O contrato deve estar vinculado a um cliente");
        }
        if (contrato.getEscritorio() == null || contrato.getEscritorio().getId() == null) {
            throw new RegraNegocioException("O contrato deve estar vinculado a um escritório");
        }
    }
}