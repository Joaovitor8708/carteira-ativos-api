package br.com.carteira.carteira.repositories;

import br.com.carteira.carteira.model.Ativo;
import br.com.carteira.carteira.model.Dividendo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DividendoRepository extends JpaRepository<Dividendo, Long> {
    List<Dividendo> findByAtivoAndDataPagamentoBetween(Ativo ativo, LocalDate inicio, LocalDate fim);

    boolean existsByAtivoAndDataPagamento(Ativo ativo, LocalDate data);

    List<Dividendo> findByDataPagamentoBetween(LocalDate inicio, LocalDate fim);
}
