package br.ufjf.amisapi.service;

import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.AreaProcesso;
import br.ufjf.amisapi.model.repository.AreaProcessoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AreaProcessoService {

    private AreaProcessoRepository repository;

    public AreaProcessoService(AreaProcessoRepository repository) {
        this.repository = repository;
    }

    public List<AreaProcesso> getAreasProcesso() {
        return repository.findAll();
    }

    // Atenção: O ID aqui é String
    public Optional<AreaProcesso> getAreaProcessoById(String id) {
        return repository.findById(id);
    }

    @Transactional
    public AreaProcesso salvar(AreaProcesso areaProcesso) {
        validar(areaProcesso);
        return repository.save(areaProcesso);
    }

    @Transactional
    public void excluir(AreaProcesso areaProcesso) {
        Objects.requireNonNull(areaProcesso.getId());
        repository.delete(areaProcesso);
    }

    public void validar(AreaProcesso areaProcesso) {
        if (areaProcesso.getNome() == null || areaProcesso.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome da área do processo inválido");
        }
    }
}