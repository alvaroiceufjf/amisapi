package br.ufjf.amisapi.model.repository;

import br.ufjf.amisapi.model.entity.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, String> {
    // Note que o ID aqui é String
}