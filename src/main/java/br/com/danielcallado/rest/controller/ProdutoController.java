package br.com.danielcallado.rest.controller;

import br.com.danielcallado.domain.entity.Produto;
import br.com.danielcallado.domain.repository.Produtos;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private Produtos produtoRepository;

    public ProdutoController(Produtos produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter um produto pelo ID")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Produto encontrado."),
                    @ApiResponse(code = 404, message = "Produto não encontrado com o ID informado.")
            }
    )
    public Produto getProdutoById(@PathVariable Integer id){
        return produtoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo produto")
    @ApiResponses(
            {
                    @ApiResponse(code = 201, message = "Produto salvo com sucesso."),
                    @ApiResponse(code = 400, message = "Erro de validação dos campos do produto.")
            }
    )
    public Produto saveProduto(@RequestBody @Valid Produto produto){
        return produtoRepository.save(produto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta um produto pelo ID")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Produto deletado."),
                    @ApiResponse(code = 404, message = "Produto não encontrado com o ID informado.")
            }
    )
    public void deleteProduto(@PathVariable Integer id){
        produtoRepository.findById(id)
                .map(produto -> {
                    produtoRepository.delete(produto);
                    return produto;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualiza um produto pelo ID")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Produto atualizado."),
                    @ApiResponse(code = 404, message = "Produto não encontrado com o ID informado.")
            }
    )
    public void updateProduto(@PathVariable Integer id,
                              @RequestBody @Valid Produto produto){
        produtoRepository.findById(id)
                .map(produtoExistente -> {
                    produto.setId(produtoExistente.getId());
                    produtoRepository.save(produto);
                    return produtoExistente;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @GetMapping
    @ApiOperation("Obtem produtos com um filtro")
    @ApiResponse(code = 200, message = "Produto encontrado.")
    public List<Produto> find(Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        return produtoRepository.findAll(example);
    }
}
