package br.com.zippydeliveryapi.controller;

import java.util.ArrayList;
import java.util.List;
import br.com.zippydeliveryapi.model.*;
import br.com.zippydeliveryapi.model.dto.request.ItensPedidoRequest;
import br.com.zippydeliveryapi.model.dto.request.PedidoRequest;
import br.com.zippydeliveryapi.model.dto.response.DashBoardResponse;
import br.com.zippydeliveryapi.service.*;
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

    @Autowired
    private final ClienteService clienteService;

    @Autowired
    private final EmpresaService empresaService;

    @Autowired
    private final ProdutoService produtoService;

    @Autowired
    private final CupomDescontoService cupomDescontoService;

    public PedidoController(PedidoService pedidoService, ClienteService clienteService, EmpresaService empresaService, ProdutoService produtoService, CupomDescontoService cupomDescontoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.empresaService = empresaService;
        this.produtoService = produtoService;
        this.cupomDescontoService = cupomDescontoService;
    }


    @PostMapping
    public ResponseEntity<Pedido> save(@RequestBody @Valid PedidoRequest request) {
        Empresa empresa = this.empresaService.findById(request.getEmpresaId());
        CupomDesconto cupom = this.cupomDescontoService.findByCodigo(request.getCodigoCupom());
      
        Pedido pedidoNovo = Pedido.builder()
                .empresa(empresa)
                .cupomDesconto(cupom)
                .dataHora(request.getDataHora())
                .formaPagamento(request.getFormaPagamento())
                .statusPagamento(request.getStatusPagamento())
                .statusPedido(request.getStatusPedido())
                .taxaEntrega(request.getTaxaEntrega())
                .cliente(this.clienteService.findById(request.getClienteId()))
                .itensPedido(this.criarListaItensPedidos(request.getItens()))
                .build();

        Pedido pedido = this.pedidoService.save(pedidoNovo);

        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
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
    public ResponseEntity<Pedido> update(@PathVariable("id") Long id, @RequestBody PedidoRequest request) {
        this.pedidoService.update(id, request.getStatusPagamento(), request.getStatusPedido());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.pedidoService.delete(id);
        return ResponseEntity.ok().build();
    }

    private List<ItensPedido> criarListaItensPedidos(List<ItensPedidoRequest> requestItensPedido) {
        List<ItensPedido> itens = new ArrayList<>();

        for (ItensPedidoRequest itensPedidoRequest : requestItensPedido) {
            Produto produto = this.produtoService.findById(itensPedidoRequest.getProdutoId());
            ItensPedido item = ItensPedido.builder()
                    .produto(produto)
                    .qtdProduto(itensPedidoRequest.getQtdProduto())
                    .valorUnitario(itensPedidoRequest.getValorUnitario())
                    .valorTotal(itensPedidoRequest.getValorUnitario() * itensPedidoRequest.getQtdProduto())
                    .build();
            itens.add(item);
        }
        return itens;
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
        return this.pedidoService.filtrarPedidosPorCliente(id);
    }

}

