package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.CategoriaEmpresa;
import br.com.zippydeliveryapi.repository.CategoriaEmpresaRepository;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaEmpresaService {

    @Autowired
    private final CategoriaEmpresaRepository repository;

    public CategoriaEmpresaService(CategoriaEmpresaRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public CategoriaEmpresa save(CategoriaEmpresa categoriaEmpresa) {
        categoriaEmpresa.setHabilitado(Boolean.TRUE);
        return this.repository.save(categoriaEmpresa);
    }

    public List<CategoriaEmpresa> findAll() {
        return this.repository.findByHabilitadoTrue();
    }

    public CategoriaEmpresa findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("CategoriaEmpresa", id));
    }

    @Transactional
    public void update(Long id, CategoriaEmpresa novaCategoria) {
        CategoriaEmpresa categoriaEmpresa = this.findById(id);
        categoriaEmpresa.setDescricao(novaCategoria.getDescricao());
        this.repository.save(categoriaEmpresa);
    }

    @Transactional
    public void delete(Long id) {
        CategoriaEmpresa categoriaEmpresa = this.findById(id);
        categoriaEmpresa.setHabilitado(Boolean.FALSE);
        this.repository.save(categoriaEmpresa);
    }
}
