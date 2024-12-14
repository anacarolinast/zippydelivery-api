package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.Cliente;
import br.com.zippydeliveryapi.model.Usuario;
import br.com.zippydeliveryapi.repository.ClienteRepository;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private final ClienteRepository repository;

    @Autowired
    private final UsuarioService usuarioService;

    public ClienteService(ClienteRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    // TODO endpoint para atualizar endereços

    @Transactional
    public Cliente save(Cliente cliente) {
        this.usuarioService.save(cliente.getUsuario());
        cliente.setHabilitado(Boolean.TRUE);
        return this.repository.save(cliente);
    }

    @Transactional
    public void update(Long id, Cliente clienteAlterado) {
        Cliente cliente = this.findById(id);
        cliente.setNome(clienteAlterado.getNome());
        cliente.setEmail(clienteAlterado.getEmail());
        cliente.setSenha(clienteAlterado.getSenha());
        this.repository.save(cliente);
    }

    public List<Cliente> findAll() {
        return this.repository.findAll();
    }

    public Cliente findByUsuarioId(Long userId) {
        Optional<Usuario> usuario = this.usuarioService.findById(userId);
        return this.repository.findByUsuario(usuario);
    }

    public Cliente findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", id));
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = this.findById(id);
        cliente.setHabilitado(Boolean.FALSE);
        cliente.setCpf("");
        cliente.setEmail("");
        cliente.getUsuario().setUsername("");
        cliente.getUsuario().setPassword("");
        this.repository.save(cliente);
    }
}
