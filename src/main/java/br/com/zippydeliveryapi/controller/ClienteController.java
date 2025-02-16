package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.Cliente;
import br.com.zippydeliveryapi.model.Endereco;
import br.com.zippydeliveryapi.model.dto.request.ClienteRequest;
import br.com.zippydeliveryapi.model.dto.request.EnderecoRequest;
import br.com.zippydeliveryapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody @Valid ClienteRequest request) {
        return new ResponseEntity<>(this.clienteService.save(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Cliente> findAll() {
        return this.clienteService.findAll();
    }

    @GetMapping("/{id}")
    public Cliente findById(@PathVariable Long id) {
        return this.clienteService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody ClienteRequest request) {
        this.clienteService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.clienteService.delete(id);
        return ResponseEntity.ok().build();
    }
  
    @GetMapping("/user/{id}")
    public Cliente findByUser(@PathVariable Long id) {
        return this.clienteService.findByUsuarioId(id);
    }

    @PostMapping("{clienteId}/endereco")
    public ResponseEntity<Endereco> saveNewAddress(@PathVariable("clienteId") Long id, @RequestBody @Valid EnderecoRequest request) {
        return new ResponseEntity<>(this.clienteService.saveNewAddress(id, request), HttpStatus.CREATED);
    }

    @GetMapping("/{clienteId}/endereco")
    public List<Endereco> findAllAddress(@PathVariable("clienteId") Long id) {
        return this.clienteService.findAllAddress(id);
    }

    @PutMapping("/{clienteId}/endereco/{enderecoId}")
    public ResponseEntity<Cliente> updateAddress(@PathVariable Long clienteId, @PathVariable Long enderecoId, @RequestBody EnderecoRequest request) {
        this.clienteService.updateAddress(clienteId, enderecoId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clienteId}/endereco/{enderecoId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long clienteId, @PathVariable Long enderecoId) {
        this.clienteService.deleteAddress(clienteId, enderecoId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{clienteId}/endereco/padrao/{enderecoId}")
    public ResponseEntity<Void> chooseDefaultAddress(@PathVariable Long clienteId, @PathVariable Long enderecoId) {
        this.clienteService.chooseDefaultAddress(clienteId, enderecoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{clienteId}/endereco/default")
    public Endereco findDefaultAddress(@PathVariable("clienteId") Long id) {
        return this.clienteService.findDefaultAddress(id);
    }
}
