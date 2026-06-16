package br.ufjf.amisapi.service;

import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Disponibilidade;
import br.ufjf.amisapi.model.repository.DisponibilidadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DisponibilidadeService {

    private DisponibilidadeRepository repository;

    public DisponibilidadeService(DisponibilidadeRepository repository) {
        this.repository = repository;
    }

    public List<Disponibilidade> getDisponibilidades() {
        return repository.findAll();
    }

    public Optional<Disponibilidade> getDisponibilidadeById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Disponibilidade salvar(Disponibilidade disponibilidade) {
        validar(disponibilidade);
        return repository.save(disponibilidade);
    }

    @Transactional
    public void excluir(Disponibilidade disponibilidade) {
        Objects.requireNonNull(disponibilidade.getId());
        repository.delete(disponibilidade);
    }

    public void validar(Disponibilidade disponibilidade) {
        if (disponibilidade.getDiaSemana() == null || disponibilidade.getDiaSemana().trim().equals("")) {
            throw new RegraNegocioException("Dia da semana inválido");
        }
        if (disponibilidade.getHoraInicio() == null || disponibilidade.getHoraFim() == null) {
            throw new RegraNegocioException("Horários de início e fim são obrigatórios");
        }
        if (disponibilidade.getUsuario() == null || disponibilidade.getUsuario().getId() == null) {
            throw new RegraNegocioException("A disponibilidade deve estar vinculada a um usuário (advogado)");
        }
    }
}