package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.Pedido;
import br.com.zippydeliveryapi.model.dto.request.PedidoRequest;
import br.com.zippydeliveryapi.model.dto.request.PedidoUpdateRequest;
import br.com.zippydeliveryapi.model.dto.response.DashBoardResponse;
import br.com.zippydeliveryapi.service.PedidoService;
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
@RequestMapping("/api/pedido")
@CrossOrigin
public class PedidoController {

    @Autowired
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> save(@RequestBody @Valid PedidoRequest request) {
        return new ResponseEntity<>(this.pedidoService.save(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Pedido> findAll() {
        return this.pedidoService.findAll();
    }

    @GetMapping("/{id}")
    public Pedido findById(@PathVariable Long id) {
        return this.pedidoService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable("id") Long id, @RequestBody PedidoUpdateRequest request) {
        this.pedidoService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.pedidoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard/{id}")
    public DashBoardResponse Dashboard(@PathVariable Long id) {
        return this.pedidoService.Dashboard(id);
    }

    @GetMapping("/dashboardMensal/{id}")
    public List<DashBoardResponse> DashboardMensal(@PathVariable Long id) {
        return this.pedidoService.DashboardMensal(id);
    }

    @GetMapping("/dashboardAll")
    public DashBoardResponse DashboardAll() {
        return this.pedidoService.DashboardAll();
    }

    @GetMapping("/dashboardMensalAll")
    public List<DashBoardResponse> DashboardMensalAll() {
        return this.pedidoService.DashboardMensalAll();
    }
    
    @GetMapping("/cliente/{id}")
    public List<Pedido> pedidosPorCliente(@PathVariable Long id) {
        return this.pedidoService.findByClienteId(id);
    }

    @GetMapping("/empresa/{id}")
    public List<Pedido> pedidosPorEmpresa(@PathVariable Long id) {
        return this.pedidoService.findByEmpresaId(id);
    }

}

