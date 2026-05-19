package br.ufjf.amisapi.model.repository;

import br.ufjf.amisapi.model.entity.TipoTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTarefaRepository extends JpaRepository<TipoTarefa, String> {
    // Note que o ID aqui é String
}