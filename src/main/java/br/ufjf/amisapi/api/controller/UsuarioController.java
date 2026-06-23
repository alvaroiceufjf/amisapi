package br.ufjf.amisapi.api.controller;

import br.ufjf.amisapi.api.dto.CredenciaisDTO;
import br.ufjf.amisapi.api.dto.TokenDTO;
import br.ufjf.amisapi.api.dto.UsuarioDTO;
import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Escritorio;
import br.ufjf.amisapi.model.entity.Usuario;
import br.ufjf.amisapi.security.JwtService;
import br.ufjf.amisapi.service.EscritorioService;
import br.ufjf.amisapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin
public class UsuarioController {

    // 1. TODAS as dependências reunidas no topo
    private final UsuarioService service;
    private final EscritorioService escritorioService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping()
    public ResponseEntity get() {
        List<Usuario> usuarios = service.getUsuarios();
        return ResponseEntity.ok(usuarios.stream().map(UsuarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = service.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(UsuarioDTO.create(usuario.get()));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuario = converter(dto);

            // 2. CRIPTOGRAFIA ANTES DE SALVAR!
            if(usuario.getSenha() != null && !usuario.getSenha().trim().equals("")) {
                String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
                usuario.setSenha(senhaCriptografada);
            }

            usuario = service.salvar(usuario);
            return new ResponseEntity(usuario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody UsuarioDTO dto) {
        if (!service.getUsuarioById(id).isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Usuario usuario = converter(dto);
            usuario.setId(id);

            // Também criptografa se o usuário estiver atualizando a senha
            if(usuario.getSenha() != null && !usuario.getSenha().trim().equals("")) {
                String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
                usuario.setSenha(senhaCriptografada);
            }

            service.salvar(usuario);
            return ResponseEntity.ok(usuario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = service.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(usuario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Método de Autenticação (Login)
    @PostMapping("/auth")
    public ResponseEntity autenticar(@RequestBody CredenciaisDTO credenciais) {
        try {
            Usuario usuario = service.getUsuarioByEmail(credenciais.getEmail())
                    .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

            if (passwordEncoder.matches(credenciais.getSenha(), usuario.getSenha())) {
                String token = jwtService.gerarToken(usuario);
                return ResponseEntity.ok(new TokenDTO(usuario.getEmail(), token));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida");
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Usuario converter(UsuarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        if (dto.getEscritorioId() != null) {
            Optional<Escritorio> escritorio = escritorioService.getEscritorioById(dto.getEscritorioId());
            if (!escritorio.isPresent()) {
                usuario.setEscritorio(null);
            } else {
                usuario.setEscritorio(escritorio.get());
            }
        }
        return usuario;
    }
}