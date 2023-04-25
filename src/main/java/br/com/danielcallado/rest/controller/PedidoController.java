package br.com.danielcallado.rest.controller;

import br.com.danielcallado.domain.entity.Pedido;
import br.com.danielcallado.rest.dto.PedidoDTO;
import br.com.danielcallado.service.PedidosService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidosService pedidosService;

    public PedidoController(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer savePedido (@RequestBody PedidoDTO dto) {
        Pedido pedido = pedidosService.salvar(dto);
        return pedido.getId();
    }
}
