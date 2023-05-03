package br.com.danielcallado.rest.controller;

import br.com.danielcallado.domain.entity.Usuario;
import br.com.danielcallado.service.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario){
        String senhaCryptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCryptografada);
        return usuarioService.salvar(usuario);
    }
}
