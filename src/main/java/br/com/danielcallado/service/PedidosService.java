package br.com.danielcallado.service;

import br.com.danielcallado.domain.entity.Pedido;
import br.com.danielcallado.domain.enums.StatusPedido;
import br.com.danielcallado.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidosService {
    Pedido salvar (PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
