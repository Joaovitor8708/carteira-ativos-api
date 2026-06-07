package br.com.carteira.carteira.repositories;

import br.com.carteira.carteira.model.Ativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtivoRepository extends JpaRepository<Ativo, Long> {

    Optional<Ativo> findByTicker(String ticker);

    boolean existsByTicker(String ticker);

}
