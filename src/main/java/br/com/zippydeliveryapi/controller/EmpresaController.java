package br.com.zippydeliveryapi.controller;

import java.util.Arrays;
import java.util.List;
import br.com.zippydeliveryapi.model.CategoriaEmpresa;
import br.com.zippydeliveryapi.model.Empresa;
import br.com.zippydeliveryapi.model.Usuario;
import br.com.zippydeliveryapi.model.dto.request.EmpresaRequest;
import br.com.zippydeliveryapi.model.dto.request.EmpresaStatusRequest;
import br.com.zippydeliveryapi.service.CategoriaEmpresaService;
import br.com.zippydeliveryapi.service.EmpresaService;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
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
@CrossOrigin
@RequestMapping("/api/empresa")
public class EmpresaController {

    @Autowired
    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }


    @PostMapping
    public ResponseEntity<Empresa> save(@RequestBody @Valid EmpresaRequest request) {
        return new ResponseEntity<>(this.empresaService.save(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Empresa> findAll() {
        return this.empresaService.findAll();
    }

    @GetMapping("/{id}")
    public Empresa findById(@PathVariable Long id) {
        return this.empresaService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> update(@PathVariable("id") Long id, @RequestBody EmpresaRequest request) {
        this.empresaService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/updatestatus")
    public ResponseEntity<Empresa> updateStatus(@PathVariable("id") Long id, @RequestBody EmpresaStatusRequest request) {
        this.empresaService.updateStatus(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.empresaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{id}")
    public Empresa findByUser(@PathVariable Long id) {
        return this.empresaService.findByUsuarioId(id);
    }
}
