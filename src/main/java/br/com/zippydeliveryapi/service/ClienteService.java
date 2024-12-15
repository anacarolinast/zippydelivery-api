package br.com.zippydeliveryapi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import br.com.zippydeliveryapi.model.Endereco;
import br.com.zippydeliveryapi.model.dto.request.EnderecoRequest;
import br.com.zippydeliveryapi.repository.EnderecoRepository;
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

    private final EnderecoRepository enderecoRepository;

    public ClienteService(ClienteRepository repository, UsuarioService usuarioService, EnderecoRepository enderecoRepository) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.enderecoRepository = enderecoRepository;
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

    @Transactional
    public Endereco saveNewAddress(Long id, EnderecoRequest request) {
        Endereco endereco = Endereco.fromRequest(request);
        Cliente cliente = this.findById(id);

        List<Endereco> listaEnderecos = cliente.getEnderecos();
        endereco.setHabilitado(Boolean.TRUE);
        listaEnderecos.add(endereco);

        cliente.setEnderecos(listaEnderecos);
        this.update(cliente.getId(), ClienteRequest.fromEntity(cliente));
        return this.enderecoRepository.save(endereco);
    }

    public List<Endereco> findAllAddress(Long id) {
        Cliente cliente = this.findById(id);
        return cliente.getEnderecos();
    }

    public Endereco findAddressById(Long addressId) {
        return this.enderecoRepository.findById(addressId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço", addressId));
    }

    @Transactional
    public void deleteAddress(Long id, Long enderecoId) {
        Cliente cliente = this.findById(id);
        Endereco endereco = this.findAddressById(enderecoId);

        endereco.setHabilitado(Boolean.FALSE);
        this.enderecoRepository.save(endereco);
        for (Endereco e : cliente.getEnderecos()) {
            if (e.getId().equals(enderecoId)) {
                e.setHabilitado(Boolean.FALSE);
                break;
            }
        }
        this.repository.save(cliente);
    }

    @Transactional
    public void updateAddress(Long id, Long enderecoId, EnderecoRequest request) {
        Cliente cliente = this.findById(id);
        Endereco endereco = this.findAddressById(enderecoId);
        endereco.setLogradouro(request.getLogradouro());
        endereco.setNumero(request.getNumero());
        endereco.setBairro(request.getBairro());
        endereco.setCidade(request.getCidade());
        endereco.setEstado(request.getEstado());
        endereco.setCep(request.getCep());
        endereco.setComplemento(request.getComplemento());
        this.enderecoRepository.save(endereco);

        for (Endereco e : cliente.getEnderecos()) {
            if (e.getId().equals(enderecoId)) {
                e.setLogradouro(request.getLogradouro());
                e.setNumero(request.getNumero());
                e.setBairro(request.getBairro());
                e.setCidade(request.getCidade());
                e.setEstado(request.getEstado());
                e.setCep(request.getCep());
                e.setComplemento(request.getComplemento());
                break;
            }
        }
        this.repository.save(cliente);
    }
}
