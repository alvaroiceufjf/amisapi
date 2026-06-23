package br.ufjf.amisapi.service;

import br.ufjf.amisapi.exception.RegraNegocioException;
import br.ufjf.amisapi.model.entity.Usuario;
import br.ufjf.amisapi.model.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> getUsuarios() {
        return repository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        validar(usuario);
        return repository.save(usuario);
    }

    @Transactional
    public void excluir(Usuario usuario) {
        Objects.requireNonNull(usuario.getId());
        repository.delete(usuario);
    }

    public void validar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome do usuário inválido");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }
        if (usuario.getEscritorio() == null || usuario.getEscritorio().getId() == null) {
            throw new RegraNegocioException("O usuário deve estar vinculado a um escritório");
        }
    }

    public Optional<Usuario> getUsuarioByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws org.springframework.security.core.userdetails.UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("Usuário não encontrado"));

        String[] roles = usuario.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}