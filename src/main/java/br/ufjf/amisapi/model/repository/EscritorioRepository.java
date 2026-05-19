package br.ufjf.amisapi.model.repository;

import br.ufjf.amisapi.model.entity.Escritorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscritorioRepository extends JpaRepository<Escritorio, Long> {
}
