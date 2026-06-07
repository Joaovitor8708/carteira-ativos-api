package br.com.carteira.carteira.repositories;

import br.com.carteira.carteira.model.Ativo;
import br.com.carteira.carteira.model.Cotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CotacaoRepository extends JpaRepository<Cotacao, Long> {

    Optional<Cotacao> findTopByAtivoOrderByDataConsultaDesc(Ativo ativo);

    boolean existsByAtivoAndDataConsulta(Ativo ativo, LocalDate data);

    List<Cotacao> findByAtivoAndDataConsultaBetween(Ativo ativo, LocalDate inicio, LocalDate fim);
}
