package br.com.danielcallado.domain.repository;

import br.com.danielcallado.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
