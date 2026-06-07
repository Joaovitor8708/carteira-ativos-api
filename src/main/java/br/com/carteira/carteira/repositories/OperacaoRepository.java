package br.com.carteira.carteira.repositories;

import br.com.carteira.carteira.enums.TipoOperacao;
import br.com.carteira.carteira.model.Ativo;
import br.com.carteira.carteira.model.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Long> {

    List<Operacao> findByAtivoTicker(String ticker);

    boolean existsByNumeroNota(String numeroNota);

    List<Operacao> findByAtivoAndTipo(Ativo ativo, TipoOperacao tipo);
}
