package br.com.zippydeliveryapi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zippydeliveryapi.model.ItensPedido;
import br.com.zippydeliveryapi.model.Pedido;
import br.com.zippydeliveryapi.model.dto.response.DashBoardResponse;
import br.com.zippydeliveryapi.repository.ItensPedidoRepository;
import br.com.zippydeliveryapi.repository.PedidoRepository;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    @Autowired
    private final PedidoRepository repository;

    @Autowired
    private final ItensPedidoRepository itensPedidoRepository;

    @Autowired
    private final CupomDescontoService cupomDescontoService;

    public PedidoService(PedidoRepository repository, ItensPedidoRepository itensPedidoRepository, CupomDescontoService cupomDescontoService) {
        this.repository = repository;
        this.itensPedidoRepository = itensPedidoRepository;
        this.cupomDescontoService = cupomDescontoService;
    }

    private List<ItensPedido> criaListaPedidos(Pedido pedido) {
        return new ArrayList<>(pedido.getItensPedido());
    }

    // Somar a taxa
    private Double calcularValorTotalPedido(List<ItensPedido> itensPedidos) {
        return itensPedidos.stream()
                .mapToDouble(ItensPedido::getValorTotal)
                .sum();
    }

    private Pedido salvarPedido(Pedido pedido, List<ItensPedido> itens) {
        pedido.setItensPedido(null);
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatusPagamento(StatusEnum.PENDENTE);
        pedido.setValorTotal(calcularValorTotalPedido(itens));
        pedido.setHabilitado(Boolean.TRUE);

        Pedido pedidoSalvo = this.repository.saveAndFlush(pedido);

        itens.forEach(item -> {
            item.setPedido(pedidoSalvo);
            item.setHabilitado(Boolean.TRUE);
            this.itensPedidoRepository.saveAndFlush(item);
        });

        pedidoSalvo.setItensPedido(itens);

        return pedidoSalvo;
    }

    @Transactional
    public Pedido save(Pedido novoPedido) {
        List<ItensPedido> itens = criaListaPedidos(novoPedido);
        Pedido pedidoSalvo = salvarPedido(novoPedido, itens);

        Optional.ofNullable(novoPedido.getCupomDesconto())
                .filter(this.cupomDescontoService::validarCupom)
                .ifPresent(cupomDesconto -> this.cupomDescontoService.aplicarCupom(pedidoSalvo, cupomDesconto));

        return pedidoSalvo;
    }

    public List<Pedido> findAll() {
        return this.repository.findAll();
    }

    public Pedido findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido", id));
    }

    public List<Pedido> findByEmpresaId(Long id) {
        return this.repository.findByEmpresaId(id);
    }

    public List<Pedido> findByClienteId(Long id) {
        return this.repository.findByClienteId(id);
    }

    @Transactional
    public void update(Long id, StatusEnum statusPagamento, StatusEnum statusPedido) {
        Pedido pedido = this.findById(id);
        pedido.setStatusPedido(statusPedido);
        pedido.setStatusPagamento(statusPagamento);
        this.repository.save(pedido);
    }

    @Transactional
    public void delete(Long id) {
        Pedido pedido = this.findById(id);
        pedido.setHabilitado(Boolean.FALSE);
        pedido.setStatusPedido(StatusEnum.CANCELADO);
        pedido.setStatusPagamento(StatusEnum.ESTORNADO);
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

    public List<Pedido> filtrarPedidosPorCliente(Long idCliente) {
        return this.findByClienteId(idCliente);
    }
}
