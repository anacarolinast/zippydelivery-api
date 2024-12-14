package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.CupomDesconto;
import br.com.zippydeliveryapi.model.dto.request.CupomDescontoRequest;
import br.com.zippydeliveryapi.service.CupomDescontoService;
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
@RequestMapping("/api/cupom")
@CrossOrigin
public class CupomDescontoController {

    @Autowired
    private final CupomDescontoService cupomDescontoService;

    public CupomDescontoController(CupomDescontoService cupomDescontoService) {
        this.cupomDescontoService = cupomDescontoService;
    }


    @PostMapping
    public ResponseEntity<CupomDesconto> save(@RequestBody @Valid CupomDescontoRequest request) {
        CupomDesconto cupom = this.cupomDescontoService.save(request.build());
        return new ResponseEntity<>(cupom, HttpStatus.CREATED);
    }

    @GetMapping
    public List<CupomDesconto> findAll() {
        return this.cupomDescontoService.findAll();
    }

    @GetMapping("/{id}")
    public CupomDesconto findById(@PathVariable Long id) {
        return this.cupomDescontoService.findById(id);
    }

    @GetMapping("/codigo/{codigo}")
    public CupomDesconto findByCodigo(@PathVariable String codigo) {
        return this.cupomDescontoService.findByCodigo(codigo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CupomDesconto> update(@PathVariable("id") Long id, @RequestBody CupomDesconto request) {
        this.cupomDescontoService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.cupomDescontoService.delete(id);
        return ResponseEntity.ok().build();
    }
}
