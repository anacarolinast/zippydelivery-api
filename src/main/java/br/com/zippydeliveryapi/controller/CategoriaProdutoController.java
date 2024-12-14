package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.CategoriaProduto;
import br.com.zippydeliveryapi.model.Produto;
import br.com.zippydeliveryapi.model.dto.request.CategoriaProdutoRequest;
import br.com.zippydeliveryapi.service.CategoriaProdutoService;
import br.com.zippydeliveryapi.service.EmpresaService;
import br.com.zippydeliveryapi.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categoria-produto")
@CrossOrigin
public class CategoriaProdutoController {

    @Autowired
    private final CategoriaProdutoService categoriaProdutoService;

    @Autowired
    private final EmpresaService empresaService;

    @Autowired
    private final ProdutoService produtoService;

    public CategoriaProdutoController(CategoriaProdutoService categoriaProdutoService, EmpresaService empresaService, ProdutoService produtoService) {
        this.categoriaProdutoService = categoriaProdutoService;
        this.empresaService = empresaService;
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<CategoriaProduto> save(@RequestBody @Valid CategoriaProdutoRequest request) {
        CategoriaProduto categoriaProduto = request.build();
        categoriaProduto.setEmpresa(this.empresaService.findById(request.getEmpresaId()));
        CategoriaProduto categoriaProdutoNova = this.categoriaProdutoService.save(categoriaProduto);
        return new ResponseEntity<>(categoriaProdutoNova, HttpStatus.CREATED);
    }

    @GetMapping
    public List<CategoriaProduto> findAll() {
        return this.categoriaProdutoService.findAll();
    }

    @GetMapping("/{id}")
    public CategoriaProduto findById(@PathVariable Long id) {
        return this.categoriaProdutoService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProduto> update(@PathVariable("id") Long id, @RequestBody CategoriaProdutoRequest request) {
        this.categoriaProdutoService.update(id, request.build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        List<Produto> produtos = this.produtoService.findByCategoriaId(id);
        for (Produto p : produtos) {
            this.produtoService.delete(p.getId());
        }
        this.categoriaProdutoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categoria-empresa/{id}")
    public List<CategoriaProduto> verCategoriasProdutosPorEmpresa(@PathVariable Long id) {
        return this.categoriaProdutoService.findByEmpresaId(id);
    }
}
