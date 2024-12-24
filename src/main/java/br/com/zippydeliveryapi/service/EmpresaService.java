package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.CategoriaEmpresa;
import br.com.zippydeliveryapi.model.Empresa;
import br.com.zippydeliveryapi.model.Endereco;
import br.com.zippydeliveryapi.model.Usuario;
import br.com.zippydeliveryapi.model.dto.request.EmpresaRequest;
import br.com.zippydeliveryapi.model.dto.request.EmpresaStatusRequest;
import br.com.zippydeliveryapi.repository.CategoriaEmpresaRepository;
import br.com.zippydeliveryapi.repository.EmpresaRepository;
import br.com.zippydeliveryapi.repository.EnderecoRepository;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class EmpresaService {

    @Autowired
    private final EmpresaRepository repository;

    @Autowired
    private final EnderecoRepository enderecoRepository;

    @Autowired
    private final CategoriaEmpresaRepository categoriaEmpresaRepository;

    @Autowired
    private final UsuarioService usuarioService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public EmpresaService(EmpresaRepository repository, EnderecoRepository enderecoRepository, CategoriaEmpresaRepository categoriaEmpresaRepository, UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.enderecoRepository = enderecoRepository;
        this.categoriaEmpresaRepository = categoriaEmpresaRepository;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Empresa save(EmpresaRequest request) {
        Usuario usuario = this.saveUser(request);
        Empresa empresa = Empresa.fromRequest(request);
        empresa.setHabilitado(Boolean.TRUE);
        empresa.setSenha(this.passwordEncoder.encode(request.getSenha()));
        empresa.setUsuario(usuario);
        return this.repository.save(empresa);
    }

    @Transactional
    private Endereco saveEndereco(EmpresaRequest request) {
        Endereco endereco = Endereco.builder()
                .logradouro(request.getEndereco().getLogradouro())
                .numero(request.getEndereco().getNumero())
                .bairro(request.getEndereco().getBairro())
                .cidade(request.getEndereco().getCidade())
                .estado(request.getEndereco().getEstado())
                .cep(request.getEndereco().getCep())
                .complemento(request.getEndereco().getComplemento())
                .build();
        return this.enderecoRepository.save(endereco);
    }

    @Transactional
    private Usuario saveUser(EmpresaRequest request) {
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
    public void update(Long id, EmpresaRequest request) {
        Empresa empresa = this.findById(id);
        CategoriaEmpresa categoriaEmpresa = this.categoriaEmpresaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("CategoriaEmpresa", request.getCategoriaId()));

        if(StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(empresa.getEmail())){
            empresa.setEmail(request.getEmail());
            empresa.getUsuario().setUsername(request.getEmail());
        }
        empresa.setCategoria(categoriaEmpresa);
        empresa.setEndereco(this.saveEndereco(request));
        empresa.setNome(StringUtils.hasText(request.getNome()) ? request.getNome() : empresa.getNome());
        empresa.setTempoEntrega((request.getTempoEntrega() > 0) ? request.getTempoEntrega() : empresa.getTempoEntrega());
        empresa.setTaxaFrete((!Objects.equals(request.getTaxaFrete(), empresa.getTaxaFrete())) ? request.getTaxaFrete() : empresa.getTaxaFrete());
        empresa.setTelefone(StringUtils.hasText(request.getTelefone()) ? request.getTelefone() : empresa.getTelefone());
        empresa.setImgPerfil(StringUtils.hasText(request.getImgPerfil()) ? request.getImgPerfil() : empresa.getImgPerfil());
        empresa.setImgCapa(StringUtils.hasText(request.getImgCapa()) ? request.getImgCapa() : empresa.getImgCapa());
        empresa.setFormasPagamento(request.getFormasPagamento());

        this.repository.save(empresa);
    }

    @Transactional
    public void updateStatus(Long id, EmpresaStatusRequest request) {
        Empresa empresa = this.findById(id);
        if (request.getStatus() == StatusEnum.ATIVO || request.getStatus() == StatusEnum.INATIVO || request.getStatus() == StatusEnum.PENDENTE) {
            if(request.getStatus() != StatusEnum.PENDENTE){
                empresa.setStatus(request.getStatus() == StatusEnum.ATIVO ? 1 : 0);
            }
            this.repository.save(empresa);
        }
    }

    public List<Empresa> findAll() {
        return this.repository.findAll();
    }

    public Empresa findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Empresa", id));
    }

    @Transactional
    public void delete(Long id) {
        Empresa empresa = this.repository.findByIdAndHabilitadoTrue(id);
        if(empresa != null){
            empresa.setHabilitado(Boolean.FALSE);
            empresa.setCnpj(String.format("deleted-%d-%s", empresa.getId(), empresa.getCnpj()));
            empresa.setEmail(String.format("deleted-%d-%s", empresa.getId(), empresa.getEmail()));
            empresa.setTelefone(String.format("deleted-%d", empresa.getId()));
            empresa.setStatus(0);
            empresa.getUsuario().setUsername(String.format("deleted-%d-%s", empresa.getUsuario().getId(), empresa.getUsuario().getUsername()));
            empresa.getUsuario().setPassword(String.format("deleted-%d-%s", empresa.getUsuario().getId(), empresa.getUsuario().getPassword()));
            empresa.getUsuario().setHabilitado(Boolean.FALSE);
            this.repository.save(empresa);
        }
    }

    public Empresa findByUsuarioId(Long userId) {
        Usuario usuario = this.usuarioService.findById(userId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Empresa", userId));
        return this.repository.findByUsuario(usuario);
    }

}
