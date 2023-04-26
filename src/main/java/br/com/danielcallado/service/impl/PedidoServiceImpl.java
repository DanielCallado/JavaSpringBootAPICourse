package br.com.danielcallado.service.impl;

import br.com.danielcallado.domain.entity.Cliente;
import br.com.danielcallado.domain.entity.ItemPedido;
import br.com.danielcallado.domain.entity.Pedido;
import br.com.danielcallado.domain.entity.Produto;
import br.com.danielcallado.domain.enums.StatusPedido;
import br.com.danielcallado.domain.repository.Clientes;
import br.com.danielcallado.domain.repository.ItensPedido;
import br.com.danielcallado.domain.repository.Pedidos;
import br.com.danielcallado.domain.repository.Produtos;
import br.com.danielcallado.exception.PedidoNaoEncontradoException;
import br.com.danielcallado.exception.RegraNegocioException;
import br.com.danielcallado.rest.dto.ItemPedidoDTO;
import br.com.danielcallado.rest.dto.PedidoDTO;
import br.com.danielcallado.service.PedidosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidosService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository.findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("ID de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItemsPedido());
        repository.save(pedido);
        itensPedidoRepository.saveAll(itemsPedido);
        pedido.setItemsPedido(itemsPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                })
                .orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if (items.isEmpty()){
            throw new RegraNegocioException("Não é possível salvar um pedido sem Items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(() -> new RegraNegocioException("ID de produto inválido: " + idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }
}
