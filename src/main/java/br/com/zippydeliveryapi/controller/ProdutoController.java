package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.Produto;
import br.com.zippydeliveryapi.model.dto.request.ProdutoRequest;
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
@RequestMapping("/api/produto")
@CrossOrigin
public class ProdutoController {

    @Autowired
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }


    @PostMapping
    public ResponseEntity<Produto> save(@RequestBody @Valid ProdutoRequest request) {
        return new ResponseEntity<>(this.produtoService.save(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Produto> findAll() {
        return this.produtoService.findAll();
    }

    @GetMapping("/{id}")
    public Produto findById(@PathVariable Long id) {
        return this.produtoService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable Long id, @RequestBody ProdutoRequest request) {
        this.produtoService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.produtoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categoria")
    public List<List<Object>> agruparPorCategoria() {
        return this.produtoService.agruparPorCategoria();
    }

     @GetMapping("/categoria-empresa/{id}")
    public List<List<Object>> agruparPorCategoriaeEmpresa(@PathVariable("id") Long empresaId) {
        return this.produtoService.agruparPorCategoriaeEmpresa(empresaId);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<Produto> findByEmpresa(@PathVariable Long empresaId) {
        return this.produtoService.findByEmpresaId(empresaId);
    }
}
