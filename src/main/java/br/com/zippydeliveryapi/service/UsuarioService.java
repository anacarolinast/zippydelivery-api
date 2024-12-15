package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.Usuario;
import br.com.zippydeliveryapi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public Usuario save(Usuario user) {
        user.setHabilitado(Boolean.TRUE);
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        return this.repository.save(user);
    }

    @Transactional
    public Usuario findByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    public Optional<Usuario> findById(Long id) {
        return this.repository.findById(id);
    }
}
