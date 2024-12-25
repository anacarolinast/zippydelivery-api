package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.CategoriaProduto;
import br.com.zippydeliveryapi.model.Empresa;
import br.com.zippydeliveryapi.model.Produto;
import br.com.zippydeliveryapi.model.dto.request.ProdutoRequest;
import br.com.zippydeliveryapi.repository.CategoriaProdutoRepository;
import br.com.zippydeliveryapi.repository.EmpresaRepository;
import br.com.zippydeliveryapi.repository.ProdutoRepository;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import br.com.zippydeliveryapi.util.exception.ProdutoException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProdutoService {

    @Autowired
    private final ProdutoRepository repository;

    @Autowired
    private final CategoriaProdutoRepository categoriaProdutoRepository;

    @Autowired final EmpresaRepository empresaRepository;

    public ProdutoService(ProdutoRepository repository, CategoriaProdutoRepository categoriaProdutoRepository, EmpresaRepository empresaRepository) {
        this.repository = repository;
        this.categoriaProdutoRepository = categoriaProdutoRepository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public Produto save(ProdutoRequest request) {
        if (!request.getDisponibilidade()) {
            throw new ProdutoException(ProdutoException.MESSAGE_DISPONIBILIDADE_PRODUTO);
        }

        CategoriaProduto categoriaProduto = this.categoriaProdutoRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("CategoriaProduto", request.getCategoriaId()));
        if(!categoriaProduto.getHabilitado()){
            throw new ProdutoException("Categoria desabilitada");
        }

        Empresa empresa = this.empresaRepository.findByIdAndHabilitadoTrue(request.getEmpresaId());
        if (empresa == null) {
            throw new EntidadeNaoEncontradaException("Empresa", request.getEmpresaId());
        }

        Produto produto = Produto.fromRequest(request);
        produto.setEmpresa(empresa);
        produto.setCategoria(categoriaProduto);
        produto.setHabilitado(Boolean.TRUE);

        return this.repository.save(produto);
    }

    public List<Produto> findAll() {
        return this.repository.findAll();
    }

    public Produto findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", id));
    }

    public List<Produto> findByCategoriaId(Long categoriaId) {
        return this.repository.findByCategoriaIdAndHabilitadoTrue(categoriaId);
    }

    public List<Produto> findByEmpresaId(Long categoriaId) {
        return this.repository.findByEmpresaIdAndHabilitadoTrue(categoriaId);
    }

    @Transactional
    public void update(Long id, ProdutoRequest request) {
        Produto produto = this.findById(id);
        CategoriaProduto categoriaProduto = this.categoriaProdutoRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("CategoriaProduto", request.getCategoriaId()));
        if(!categoriaProduto.getHabilitado()){
            throw new ProdutoException("Categoria desabilitada");
        }
        produto.setCategoria(categoriaProduto);
        produto.setDescricao(request.getDescricao());
        produto.setTitulo(request.getTitulo());
        produto.setImagem(request.getImagem());
        produto.setPreco(request.getPreco());
        produto.setDisponibilidade(request.getDisponibilidade());
        this.repository.save(produto);
    }

    @Transactional
    public void delete(Long id) {
        Produto produto = this.findById(id);
        produto.setHabilitado(Boolean.FALSE);
        this.repository.save(produto);
    }

    public List<List<Object>> agruparPorCategoria() {
        List<Object[]> resultados = this.repository.agruparPorCategoria();
        Map<Long, List<Object>> categoriasMap = new HashMap<>();

        for (Object[] resultado : resultados) {
            CategoriaProduto categoria = (CategoriaProduto) resultado[0];
            Produto produto = (Produto) resultado[1];

            if (produto != null && produto.getHabilitado()) {
                categoriasMap.computeIfAbsent(categoria.getId(), k -> new ArrayList<>()).add(produto);
            }
        }
        return new ArrayList<>(categoriasMap.values());
    }

    public List<List<Object>> agruparPorCategoriaeEmpresa(Long id) {
        List<Object[]> resultados = this.repository.findByEmpresaGroupByCategoria(id);
        Map<Long, List<Object>> categoriasMap = new HashMap<>();

        for (Object[] resultado : resultados) {
            CategoriaProduto categoria = (CategoriaProduto) resultado[0];
            Object produto = resultado[1];

            if (produto != null) {
                categoriasMap.computeIfAbsent(categoria.getId(), k -> new ArrayList<>()).add(produto);
            }
        }
        return new ArrayList<>(categoriasMap.values());
    }
}
