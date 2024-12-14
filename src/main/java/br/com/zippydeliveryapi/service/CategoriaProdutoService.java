package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.CategoriaProduto;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import br.com.zippydeliveryapi.repository.CategoriaProdutoRepository;

@Service
public class CategoriaProdutoService {

    @Autowired
    private final CategoriaProdutoRepository repository;

    public CategoriaProdutoService(CategoriaProdutoRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public CategoriaProduto save(CategoriaProduto categoriaProduto) {
        categoriaProduto.setHabilitado(Boolean.TRUE);
        return this.repository.save(categoriaProduto);
    }

    public List<CategoriaProduto> findAll() {
        return this.repository.findAll();
    }

    public CategoriaProduto findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("CategoriaProduto", id));
    }

    public List<CategoriaProduto> findByEmpresaId(Long id) {
        return this.repository.findByEmpresaId(id);
    }

    @Transactional
    public void update(Long id, CategoriaProduto novaCategoria) {
        CategoriaProduto categoriaProduto = this.findById(id);
        categoriaProduto.setDescricao(novaCategoria.getDescricao());
        this.repository.save(categoriaProduto);
    }

    @Transactional
    public void delete(Long id) {
        CategoriaProduto categoriaProduto = this.findById(id);
        categoriaProduto.setHabilitado(Boolean.FALSE);
        this.repository.save(categoriaProduto);
    }
}
