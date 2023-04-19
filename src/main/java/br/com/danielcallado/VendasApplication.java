package br.com.danielcallado;

import br.com.danielcallado.domain.entity.Cliente;
import br.com.danielcallado.domain.entity.Pedido;
import br.com.danielcallado.domain.repository.Clientes;
import br.com.danielcallado.domain.repository.Pedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@SpringBootApplication
public class VendasApplication {

    @Bean
    public CommandLineRunner init(@Autowired Clientes clientes, @Autowired Pedidos pedidos){
        return args -> {
            System.out.println("Salvando Clientes");
            Cliente cliente = clientes.save(new Cliente("Daniel"));
            clientes.save(new Cliente("Beatriz"));

            Pedido p = new Pedido();
            p.setCliente(cliente);
            p.setDataPedido(LocalDate.now());
            p.setTotal(BigDecimal.valueOf(100));

            pedidos.save(p);

            List<Cliente> lista = clientes.encontrarPorNome("Daniel");
            lista.forEach(System.out::println);

//            Cliente clieteFetchPedidos = clientes.findClieteFetchPedidos(cliente.getId());
//            System.out.println(clieteFetchPedidos);
//            System.out.println(clieteFetchPedidos.getPedidos());

            pedidos.findByCliente(cliente).forEach(System.out::println);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}
