package br.com.danielcallado.rest.controller;

import br.com.danielcallado.domain.entity.ItemPedido;
import br.com.danielcallado.domain.entity.Pedido;
import br.com.danielcallado.domain.enums.StatusPedido;
import br.com.danielcallado.rest.dto.AtualizacaoStatusPedidoDTO;
import br.com.danielcallado.rest.dto.InformacoesItemPedidoDto;
import br.com.danielcallado.rest.dto.InformacoesPedidoDTO;
import br.com.danielcallado.rest.dto.PedidoDTO;
import br.com.danielcallado.service.PedidosService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidosService pedidosService;

    public PedidoController(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo Pedido")
    @ApiResponses(
            {
                    @ApiResponse(code = 201, message = "Pedido salvo com sucesso."),
                    @ApiResponse(code = 400, message = "Erro de validação dos campos do pedido.")
            }
    )
    public Integer savePedido (@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = pedidosService.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter um pedido pelo ID")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Pedido encontrado."),
                    @ApiResponse(code = 404, message = "Pedido não encontrado com o ID informado.")
            }
    )
    public InformacoesPedidoDTO getById(@PathVariable Integer id){
        return pedidosService
                .obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualiza um Pedido pelo ID")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Pedido atualizado."),
                    @ApiResponse(code = 404, message = "Pedido não encontrado com o ID informado.")
            }
    )
    public void updateStatus(@PathVariable Integer id,
                             @RequestBody AtualizacaoStatusPedidoDTO dto){
        String novoStatus = dto.getNovoStatus();
        pedidosService.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
    }

    private InformacoesPedidoDTO converter(Pedido pedido){
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nome(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItemsPedido()))
                .build();
    }

    private List<InformacoesItemPedidoDto> converter(List<ItemPedido> itens){
        if (CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }
        return itens.stream().map(
                item -> InformacoesItemPedidoDto
                        .builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()).collect(Collectors.toList());
    }
}
