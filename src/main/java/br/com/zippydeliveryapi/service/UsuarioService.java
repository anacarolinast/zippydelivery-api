package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.Usuario;
import br.com.zippydeliveryapi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private final UsuarioRepository repository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Usuario save(Usuario user) {
        user.setHabilitado(Boolean.TRUE);
        user.setUsername(user.getUsername());
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.repository.save(user);
    }

    @Transactional
    public Usuario findByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    public Optional<Usuario> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = this.repository.findByUsername(username);
        if (usuario == null || !usuario.getHabilitado()) {
            throw new UsernameNotFoundException("User not found or disabled");
        }
        return new org.springframework.security.core.userdetails.User(usuario.getUsername(), usuario.getPassword(), new ArrayList<>());
    }

}
