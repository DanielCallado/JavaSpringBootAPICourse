package br.com.danielcallado.domain.repository;

import br.com.danielcallado.domain.entity.Cliente;
import br.com.danielcallado.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface Pedidos extends JpaRepository<Pedido, Integer> {

    Set<Pedido> findByCliente(Cliente cliente);
}
