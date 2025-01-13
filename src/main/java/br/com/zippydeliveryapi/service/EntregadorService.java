package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.Entregador;
import br.com.zippydeliveryapi.model.Usuario;
import br.com.zippydeliveryapi.model.dto.request.EntregadorRequest;
import br.com.zippydeliveryapi.repository.EntregadorRepository;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class EntregadorService {

    @Autowired
    private final EntregadorRepository repository;

    @Autowired
    private final UsuarioService usuarioService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public EntregadorService(EntregadorRepository repository, UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }


    public List<Entregador> findAll() {
        return this.repository.findAll();
    }

    public Entregador findByUsuarioId(Long userId) {
        Optional<Usuario> usuario = this.usuarioService.findById(userId);
        return this.repository.findByUsuario(usuario);
    }

    public Entregador findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Entregador", id));
    }

    @Transactional
    public Entregador save(EntregadorRequest request) {
        Usuario usuario = this.saveUser(request);
        Entregador entregador = Entregador.fromRequest(request);
        entregador.setHabilitado(Boolean.TRUE);
        entregador.setSenha(this.passwordEncoder.encode(entregador.getSenha()));
        entregador.setUsuario(usuario);
        return this.repository.save(entregador);
    }

    @Transactional
    public void update(Long id, EntregadorRequest request) {
        Entregador entregador = this.findById(id);

        if(StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(entregador.getEmail())){
            entregador.setEmail(request.getEmail());
            entregador.getUsuario().setUsername(request.getEmail());
        }
        if(StringUtils.hasText(request.getSenha()) && !request.getSenha().equals(entregador.getSenha())){
            entregador.setSenha(request.getSenha());
            entregador.getUsuario().setPassword(request.getSenha());
        }
        entregador.setNome(request.getNome());
        entregador.setDataNascimento(request.getDataNascimento());
        entregador.setVeiculo(request.getVeiculo());
        entregador.setPlaca(request.getPlaca());
        entregador.setTelefone(request.getTelefone());
        this.repository.save(entregador);
    }

    @Transactional
    public void updateStatus(Long id, StatusEnum novoStatus) {
        Entregador entregador = this.findById(id);
        entregador.setStatus(novoStatus.getCodigo());
        this.repository.save(entregador);
    }

    @Transactional
    public void delete(Long id) {
        Entregador entregador = this.repository.findByIdAndHabilitadoTrue(id);
        if(entregador != null) {
            entregador.setHabilitado(Boolean.FALSE);
            entregador.setCpf(String.format("deleted-%d-%s", entregador.getId(), entregador.getCpf()));
            entregador.setEmail(String.format("deleted-%d-%s", entregador.getId(), entregador.getEmail()));
            entregador.getUsuario().setUsername(String.format("deleted-%d-%s", entregador.getUsuario().getId(), entregador.getUsuario().getUsername()));
            entregador.getUsuario().setPassword(String.format("deleted-%d-%s", entregador.getUsuario().getId(), entregador.getUsuario().getPassword()));
            entregador.getUsuario().setHabilitado(Boolean.FALSE);
            this.repository.save(entregador);
        }
    }

    @Transactional
    private Usuario saveUser(EntregadorRequest request) {
        Usuario usuario = null;
        try {
            usuario = this.usuarioService.findByUsername(request.getEmail());
            if(usuario == null){
                usuario = Usuario.builder()
                        .roles(Arrays.asList(Usuario.ROLE_ENTREGADOR))
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
}
