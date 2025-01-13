package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.Entregador;
import br.com.zippydeliveryapi.model.dto.request.EntregadorRequest;
import br.com.zippydeliveryapi.model.dto.request.EntregadorStatusRequest;
import br.com.zippydeliveryapi.service.EntregadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entregador")
@CrossOrigin
public class EntregadorController {
    
    @Autowired
    private final EntregadorService entregadorService;

    public EntregadorController(EntregadorService entregadorService) {
        this.entregadorService = entregadorService;
    }

    @GetMapping
    public List<Entregador> findAll() {
        return this.entregadorService.findAll();
    }

    @PostMapping
    public ResponseEntity<Entregador> save(@RequestBody @Valid EntregadorRequest request) { // se tiver tempo melhoro isso, o ideal é retornar um dto, sem info sensíveis
        return new ResponseEntity<>(this.entregadorService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entregador> update(@PathVariable("id") Long id, @RequestBody EntregadorRequest request) {
        this.entregadorService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.entregadorService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{id}")
    public Entregador findByUser(@PathVariable Long id) {
        return this.entregadorService.findByUsuarioId(id);
    }

    @PutMapping("/{id}/updatestatus")
    public ResponseEntity<Entregador> updateStatus(@PathVariable("id") Long id, @RequestBody EntregadorStatusRequest request) {
        this.entregadorService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok().build();
    }
}
