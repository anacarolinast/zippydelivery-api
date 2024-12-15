package br.com.zippydeliveryapi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import br.com.zippydeliveryapi.model.Cliente;
import br.com.zippydeliveryapi.model.Usuario;
import br.com.zippydeliveryapi.model.dto.request.ClienteRequest;
import br.com.zippydeliveryapi.repository.ClienteRepository;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

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
    public Cliente save(ClienteRequest request) {
        Usuario usuario = this.saveUser(request);
        Cliente cliente = Cliente.fromRequest(request);
        cliente.setUsuario(usuario);
        cliente.setEnderecos(new ArrayList<>());
        cliente.setHabilitado(Boolean.TRUE);
        return this.repository.save(cliente);
    }

    @Transactional
    private Usuario saveUser(ClienteRequest request) {
        Usuario usuario = null;
        try {
            usuario = this.usuarioService.findByUsername(request.getEmail());
            if(usuario == null){
                usuario = Usuario.builder()
                        .roles(Arrays.asList(Usuario.ROLE_EMPRESA))
                        .username(request.getEmail())
                        .password(request.getSenha())
                        .build();
                return this.usuarioService.save(usuario);
            } else {
                throw new IllegalArgumentException("Já existe um usuário com o e-mail: " + request.getEmail());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o usuário: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void update(Long id, ClienteRequest request) {
        Cliente cliente = this.findById(id);
        if(StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(cliente.getEmail())){
            cliente.setEmail(request.getEmail());
            cliente.getUsuario().setUsername(request.getEmail());
        }
        cliente.setNome(request.getNome());
        cliente.setSenha(request.getSenha());
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
        Cliente cliente = this.repository.findByIdAndHabilitadoTrue(id);
        if(cliente != null){
            cliente.setHabilitado(Boolean.FALSE);
            cliente.setCpf(String.format("deleted-%d-%s", cliente.getId(), cliente.getCpf()));
            cliente.setEmail(String.format("deleted-%d-%s", cliente.getId(), cliente.getEmail()));
            cliente.getUsuario().setUsername(String.format("deleted-%d-%s", cliente.getUsuario().getId(), cliente.getUsuario().getUsername()));
            cliente.getUsuario().setPassword(String.format("deleted-%d-%s", cliente.getUsuario().getId(), cliente.getUsuario().getPassword()));
            cliente.getUsuario().setHabilitado(Boolean.FALSE);
            this.repository.save(cliente);
        }
    }
}
