package br.com.danielcallado.rest.controller;

import br.com.danielcallado.domain.entity.Cliente;
import br.com.danielcallado.domain.repository.Clientes;
import io.swagger.annotations.Api;
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
@RequestMapping("/api/clientes")
@Api("Api Clientes")
public class ClienteController {

    private Clientes clienteRepository;

    public ClienteController(Clientes clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter um cliente pelo ID")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Cliente encontrado."),
                    @ApiResponse(code = 404, message = "Cliente não encontrado com o ID informado.")
            }
    )
    public Cliente getClienteById(@PathVariable Integer id){
        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo cliente")
    @ApiResponses(
            {
                    @ApiResponse(code = 201, message = "Cliente salvo com sucesso."),
                    @ApiResponse(code = 400, message = "Erro de validação dos campos do cliente.")
            }
    )
    public Cliente saveCliente(@RequestBody @Valid Cliente cliente){
        return clienteRepository.save(cliente);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta um cliente pelo ID")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Cliente deletado."),
                    @ApiResponse(code = 404, message = "Cliente não encontrado com o ID informado.")
            }
    )
    public void deleteCliente(@PathVariable Integer id){
        clienteRepository.findById(id)
                .map( cliente -> {
                    clienteRepository.delete(cliente);
                    return cliente;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualiza um cliente pelo ID")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Cliente atualizado."),
                    @ApiResponse(code = 404, message = "Cliente não encontrado com o ID informado.")
            }
    )
    public void updateCliente(@PathVariable Integer id,
                              @RequestBody @Valid Cliente cliente) {
        clienteRepository
            .findById(id)
            .map( clienteExistente -> {
                cliente.setId(clienteExistente.getId());
                clienteRepository.save(cliente);
                return clienteExistente;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @GetMapping
    @ApiOperation("Obtem clientes com um filtro")
    @ApiResponse(code = 200, message = "Cliente encontrado.")
    public List<Cliente> find(Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        return clienteRepository.findAll(example);
    }
}
