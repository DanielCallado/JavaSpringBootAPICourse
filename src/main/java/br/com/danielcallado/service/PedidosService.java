package br.com.danielcallado.service;

import br.com.danielcallado.domain.entity.Pedido;
import br.com.danielcallado.rest.dto.PedidoDTO;

public interface PedidosService {
    Pedido salvar (PedidoDTO dto);
}
