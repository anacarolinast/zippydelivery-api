package br.com.zippydeliveryapi.controller;

import br.com.zippydeliveryapi.model.CategoriaEmpresa;
import br.com.zippydeliveryapi.model.dto.request.CategoriaEmpresaRequest;
import br.com.zippydeliveryapi.service.CategoriaEmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/categoria-empresa")
public class CategoriaEmpresaController {

    @Autowired
    private final CategoriaEmpresaService categoriaEmpresaService;

    public CategoriaEmpresaController(CategoriaEmpresaService categoriaEmpresaService) {
        this.categoriaEmpresaService = categoriaEmpresaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaEmpresa> save(@RequestBody @Valid CategoriaEmpresaRequest request) {
        CategoriaEmpresa categoriaEmpresa = request.build();
        CategoriaEmpresa categoriaEmpresaNova = this.categoriaEmpresaService.save(categoriaEmpresa);
        return new ResponseEntity<>(categoriaEmpresaNova, HttpStatus.CREATED);
    }

    @GetMapping
    public List<CategoriaEmpresa> findAll() {
        return this.categoriaEmpresaService.findAll();
    }

    @GetMapping("/{id}")
    public CategoriaEmpresa findById(@PathVariable Long id) {
        return this.categoriaEmpresaService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaEmpresa> update(@PathVariable("id") Long id, @RequestBody CategoriaEmpresaRequest request) {
        this.categoriaEmpresaService.update(id, request.build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.categoriaEmpresaService.delete(id);
        return ResponseEntity.ok().build();
    }
}
