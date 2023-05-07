package br.com.danielcallado.rest.controller;

import br.com.danielcallado.domain.entity.Usuario;
import br.com.danielcallado.exception.SenhaInvalidaException;
import br.com.danielcallado.rest.dto.CredenciaisDTO;
import br.com.danielcallado.rest.dto.TokenDTO;
import br.com.danielcallado.security.jwt.JwtService;
import br.com.danielcallado.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo usuario")
    @ApiResponses(
            {
                    @ApiResponse(code = 201, message = "Usuário salvo com sucessi."),
                    @ApiResponse(code = 400, message = "Erro de validação dos campos do usuário.")
            }
    )
    public Usuario salvar(@RequestBody @Valid Usuario usuario){
        String senhaCryptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCryptografada);
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    @ApiOperation("Autentica um usuário existente.")
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Cliente validado com sucesso."),
                    @ApiResponse(code = 401, message = "Usuário não autenticado.")
            }
    )
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciaisDTO){
        try {
            Usuario usuario = Usuario.builder()
                    .login(credenciaisDTO.getLogin())
                    .senha(credenciaisDTO.getSenha()).build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);
        } catch (SenhaInvalidaException | UsernameNotFoundException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }
}
