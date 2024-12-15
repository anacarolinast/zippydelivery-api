package br.com.zippydeliveryapi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import br.com.zippydeliveryapi.model.*;
import br.com.zippydeliveryapi.model.dto.request.ItensPedidoRequest;
import br.com.zippydeliveryapi.model.dto.request.PedidoRequest;
import br.com.zippydeliveryapi.model.dto.request.PedidoUpdateRequest;
import br.com.zippydeliveryapi.repository.*;
import br.com.zippydeliveryapi.util.exception.ProdutoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.zippydeliveryapi.model.dto.response.DashBoardResponse;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.springframework.util.StringUtils;

@Service
public class PedidoService {

    @Autowired
    private final PedidoRepository repository;

    @Autowired
    private final CupomDescontoService cupomDescontoService;

    @Autowired
    private final EmpresaRepository empresaRepository;

    @Autowired
    private final ClienteRepository clienteRepository;

    @Autowired
    private final EnderecoRepository enderecoRepository;

    @Autowired
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository repository, CupomDescontoService cupomDescontoService, EmpresaRepository empresaRepository, ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.cupomDescontoService = cupomDescontoService;
        this.empresaRepository = empresaRepository;
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.produtoRepository = produtoRepository;
    }


    @Transactional
    public Pedido save(PedidoRequest request) {
        if(request.getItens().isEmpty()) throw new ProdutoException(ProdutoException.MESSAGE_DISPONIBILIDADE_PRODUTO);

        Cliente cliente = this.clienteRepository.findByIdAndHabilitadoTrue(request.getClienteId());
        if (cliente == null) throw new EntidadeNaoEncontradaException("Cliente", request.getClienteId());

        Empresa empresa = this.empresaRepository.findByIdAndHabilitadoTrue(request.getEmpresaId());
        if (empresa == null) throw new EntidadeNaoEncontradaException("Empresa", request.getEmpresaId());

        CupomDesconto cupomDesconto = null;
        if (StringUtils.hasText(request.getCodigoCupom())) {
            cupomDesconto = this.cupomDescontoService.findByCodigo(request.getCodigoCupom());
        }

        Endereco endereco = this.enderecoRepository.findById(request.getEnderecoEntregaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereco de Entrega", request.getEnderecoEntregaId()));

        List<ItensPedido> listaItens = this.criaListaPedidos(request.getItens());
        Double valorTotalPedido = this.calcularValorTotalPedido(listaItens, cupomDesconto);

        Pedido pedido = Pedido.fromRequest(request);
        pedido.setCliente(cliente);
        pedido.setEmpresa(empresa);
        pedido.setCupomDesconto(cupomDesconto);
        pedido.setEnderecoEntrega(endereco);
        pedido.setTaxaEntrega(empresa.getTaxaFrete());
        pedido.setItensPedido(listaItens);
        pedido.setValorTotal(valorTotalPedido);
        pedido.setHabilitado(Boolean.TRUE);
        pedido.setDataHora(LocalDateTime.now());

        return this.repository.save(pedido);
    }

    private List<ItensPedido> criaListaPedidos(List<ItensPedidoRequest> itens) {
        return itens.stream()
                .map(this::converteParaItensPedido)
                .toList();
    }

    private ItensPedido converteParaItensPedido(ItensPedidoRequest request) {
        Produto produto = this.produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto", request.getProdutoId()));
        return ItensPedido.builder()
                .produto(produto)
                .qtdProduto(request.getQtdProduto())
                .valorUnitario(request.getValorUnitario())
                .valorTotal(request.getValorUnitario() * request.getQtdProduto())
                .build();
    }

    private Double calcularValorTotalPedido(List<ItensPedido> itensPedidos, CupomDesconto cupomDesconto) {
        Double totalItens = itensPedidos.stream()
                .mapToDouble(ItensPedido::getValorTotal)
                .sum();
        if(cupomDesconto != null && this.cupomDescontoService.validarCupom(cupomDesconto)){
            return this.cupomDescontoService.aplicarCupom(totalItens, cupomDesconto);
        }
        return totalItens;
    }

    public List<Pedido> findAll() {
        return this.repository.findAll();
    }

    public Pedido findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido", id));
    }

    public List<Pedido> findByEmpresaId(Long empresaId) {
        return this.repository.findByEmpresaId(empresaId);
    }

    public List<Pedido> findByClienteId(Long clienteId) {
        return this.repository.findByClienteId(clienteId);
    }

    @Transactional
    public void update(Long id, PedidoUpdateRequest request) {
        Pedido pedido = this.findById(id);
        Endereco enderecoEntrega = this.enderecoRepository.findById(request.getEnderecoEntregaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereco", id));

        pedido.setFormaPagamento(request.getFormaPagamento().getCodigo());
        pedido.setStatusPedido(request.getStatusPedido().getCodigo());
        pedido.setStatusPagamento(request.getStatusPagamento().getCodigo());
        pedido.setEnderecoEntrega(enderecoEntrega);
        this.repository.save(pedido);
    }

    @Transactional
    public void delete(Long id) {
        Pedido pedido = this.findById(id);
        pedido.setHabilitado(Boolean.FALSE);
        pedido.setStatusPedido(StatusEnum.CANCELADO.getCodigo());
        pedido.setStatusPagamento(StatusEnum.ESTORNADO.getCodigo());
        this.repository.save(pedido);
    }

    public DashBoardResponse Dashboard(Long id) {
        List<Pedido> pedidos = this.findByEmpresaId(id);
        DashBoardResponse response = new DashBoardResponse();
        Double vendasHojeValor = 0.0;

        response.setVendasTotais(pedidos.size());
        response.setFatoramentoTotal(0.0);
        response.setVendaHoje(0);

        for (Pedido pedido : pedidos) {
            response.setFatoramentoTotal(response.getFatoramentoTotal() + pedido.getValorTotal());
            if (pedido.getDataHora().toLocalDate().equals(LocalDate.now())) {
                response.setVendaHoje(response.getVendaHoje() + 1);
                vendasHojeValor = vendasHojeValor + pedido.getValorTotal();
            }
        }
        response.setFaturamentoMedio(vendasHojeValor / response.getVendaHoje());

        return response;
    }

    public List<DashBoardResponse> DashboardMensal(Long id) {
        List<Pedido> pedidos = this.findByEmpresaId(id);
        List<DashBoardResponse> responses = new ArrayList<>();

        for (Integer i = 0; i < 12; i++) {
            responses.add(new DashBoardResponse());
        }
        for (Pedido pedido : pedidos) {
            Integer mes = pedido.getDataHora().getMonthValue() - 1;
            DashBoardResponse response = responses.get(mes);

            if (response.getVendasTotais() == null) {
                response.setVendasTotais(0);
            }
            response.setVendasTotais(response.getVendasTotais() + 1);
            if (response.getFatoramentoTotal() == null) {
                response.setFatoramentoTotal(0.0);
            }
            response.setFatoramentoTotal(response.getFatoramentoTotal() + pedido.getValorTotal());
        }
        for (DashBoardResponse dashBoardResponse : responses) {
            if (dashBoardResponse.getVendasTotais() == null) {
                dashBoardResponse.setVendasTotais(0);
            }
            if (dashBoardResponse.getFatoramentoTotal() == null) {
                dashBoardResponse.setFatoramentoTotal(0.0);
            }
            if (dashBoardResponse.getVendasTotais() > 0) {
                dashBoardResponse.setFaturamentoMedio(
                        dashBoardResponse.getFatoramentoTotal() / dashBoardResponse.getVendasTotais());
            }
        }
        return responses;
    }

    public DashBoardResponse DashboardAll() {
        List<Pedido> pedidos = this.findAll();
        DashBoardResponse response = new DashBoardResponse();
        Double vendasHojeValor = 0.0;

        response.setVendasTotais(pedidos.size());
        response.setFatoramentoTotal(0.0);
        response.setVendaHoje(0);

        for (Pedido pedido : pedidos) {
            response.setFatoramentoTotal(response.getFatoramentoTotal() + pedido.getValorTotal());
            if (pedido.getDataHora().toLocalDate().equals(LocalDate.now())) {
                response.setVendaHoje(response.getVendaHoje() + 1);
                vendasHojeValor = vendasHojeValor + pedido.getValorTotal();
            }
        }
        response.setFaturamentoMedio(vendasHojeValor / response.getVendaHoje());

        return response;
    }

    public List<DashBoardResponse> DashboardMensalAll() {
        List<Pedido> pedidos = this.findAll();
        List<DashBoardResponse> responses = new ArrayList<>();

        for (Integer i = 0; i < 12; i++) {
            responses.add(new DashBoardResponse());
        }
        for (Pedido pedido : pedidos) {
            Integer mes = pedido.getDataHora().getMonthValue() - 1;
            DashBoardResponse response = responses.get(mes);

            if (response.getVendasTotais() == null) {
                response.setVendasTotais(0);
            }
            response.setVendasTotais(response.getVendasTotais() + 1);

            if (response.getFatoramentoTotal() == null) {
                response.setFatoramentoTotal(0.0);
            }
            response.setFatoramentoTotal(response.getFatoramentoTotal() + pedido.getValorTotal());
        }
        for (DashBoardResponse dashBoardResponse : responses) {
            if (dashBoardResponse.getVendasTotais() == null) {
                dashBoardResponse.setVendasTotais(0);
            }
            if (dashBoardResponse.getFatoramentoTotal() == null) {
                dashBoardResponse.setFatoramentoTotal(0.0);
            }
            if (dashBoardResponse.getVendasTotais() > 0) {
                dashBoardResponse.setFaturamentoMedio(
                        dashBoardResponse.getFatoramentoTotal() / dashBoardResponse.getVendasTotais());
            }
        }
        return responses;
    }
}
