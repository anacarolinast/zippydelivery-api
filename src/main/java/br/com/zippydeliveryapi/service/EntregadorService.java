package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.Entregador;
import br.com.zippydeliveryapi.model.Usuario;
import br.com.zippydeliveryapi.repository.EntregadorRepository;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EntregadorService {

    @Autowired
    private final EntregadorRepository repository;

    @Autowired
    private final UsuarioService usuarioService;

    public EntregadorService(EntregadorRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
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
    public Entregador save(Entregador entregador) {
        this.usuarioService.save(entregador.getUsuario());
        entregador.setHabilitado(Boolean.TRUE);
        entregador.setStatus(StatusEnum.PENDENTE.getCodigo());
        return this.repository.save(entregador);
    }

    @Transactional
    public void update(Long id, Entregador entregadorAlterado) {
        Entregador entregador = this.findById(id);
        entregador.setNome(entregadorAlterado.getNome());
        entregador.setEmail(entregadorAlterado.getEmail());
        entregador.setSenha(entregadorAlterado.getSenha());
        entregador.setDataNascimento(entregadorAlterado.getDataNascimento());
        entregador.setVeiculo(entregadorAlterado.getVeiculo());
        entregador.setPlaca(entregadorAlterado.getPlaca());
        entregador.setTelefone(entregadorAlterado.getTelefone());
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
        Entregador entregador = this.findById(id);
        entregador.setHabilitado(Boolean.FALSE);
        entregador.setCpf(String.format("deleted-%d-%s", entregador.getId(), entregador.getCpf()));
        entregador.setEmail(String.format("deleted-%d-%s", entregador.getId(), entregador.getEmail()));
        entregador.getUsuario().setUsername(String.format("deleted-%d-%s", entregador.getUsuario().getId(), entregador.getUsuario().getUsername()));
        entregador.getUsuario().setPassword(String.format("deleted-%d-%s", entregador.getUsuario().getId(), entregador.getUsuario().getPassword()));
        entregador.getUsuario().setHabilitado(Boolean.FALSE);
        this.repository.save(entregador);
    }
}
