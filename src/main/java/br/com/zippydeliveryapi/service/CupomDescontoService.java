package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.CupomDesconto;
import br.com.zippydeliveryapi.repository.CupomDescontoRepository;
import br.com.zippydeliveryapi.util.exception.CupomDescontoException;
import br.com.zippydeliveryapi.model.Pedido;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class CupomDescontoService {

    @Autowired
    private final CupomDescontoRepository repository;

    public CupomDescontoService(CupomDescontoRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public CupomDesconto save(CupomDesconto cupom) {
        this.validateDateRange(cupom);
        cupom.setHabilitado(Boolean.TRUE);
        return this.repository.save(cupom);
    }

    public List<CupomDesconto> findAll() {
        return this.repository.findAll();
    }

    public CupomDesconto findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new CupomDescontoException(String.format("Cupom com id %s não encontrado", id)));
    }

    @Transactional
    public CupomDesconto findByCodigo(String codigo) {
        return this.repository.findByCodigo(codigo)
                .orElseThrow(() -> new CupomDescontoException(String.format("Cupom com codigo %s não encontrado", codigo)));
    }

    @Transactional
    public void update(Long id, CupomDesconto cupomAlterado) {
        CupomDesconto cupomDesconto = this.findById(id);
        cupomDesconto.setCodigo(cupomAlterado.getCodigo());
        cupomDesconto.setPercentualDesconto(cupomAlterado.getPercentualDesconto());
        cupomDesconto.setValorDesconto(cupomAlterado.getValorDesconto());
        cupomDesconto.setValorMinimoPedidoPermitido(cupomAlterado.getValorMinimoPedidoPermitido());
        cupomDesconto.setQuantidadeMaximaUso(cupomAlterado.getQuantidadeMaximaUso());
        cupomDesconto.setInicioVigencia(cupomAlterado.getInicioVigencia());
        cupomDesconto.setFimVigencia(cupomAlterado.getFimVigencia());
        this.repository.save(cupomDesconto);
    }

    @Transactional
    public void delete(Long id) {
        CupomDesconto cupomDesconto = this.findById(id);
        cupomDesconto.setHabilitado(Boolean.FALSE);
        cupomDesconto.setCodigo("");
        this.repository.save(cupomDesconto);
    }

    private void validateDateRange(CupomDesconto cupomDesconto) {
        Objects.requireNonNull(cupomDesconto, "CupomDesconto não pode ser nulo");
        if (!cupomDesconto.getFimVigencia().isAfter(cupomDesconto.getInicioVigencia())) {
            throw new CupomDescontoException(CupomDescontoException.MESSAGE_INVALID_DATE);
        }
    }

    public boolean validarCupom(CupomDesconto cupom) {
        LocalDate date = LocalDate.now();
        LocalDate inicio = cupom.getInicioVigencia();
        LocalDate fim = cupom.getFimVigencia();
        return (date.isEqual(inicio) || date.isAfter(inicio)) && (date.isEqual(fim) || date.isBefore(fim));
    }

    private void aplicarDescontoNoPedido(Pedido pedido, Double desconto) {
        Double valorTotalComDesconto = pedido.getValorTotal() - desconto;
        pedido.setValorTotal(valorTotalComDesconto);
    }

    public void aplicarCupom(Pedido pedido, CupomDesconto cupom) {
        Double desconto = 0.0;

        if (cupom.getPercentualDesconto() != null && cupom.getPercentualDesconto() != 0.0) {
            desconto = pedido.getValorTotal() * (cupom.getPercentualDesconto() / 100);
        } else if (cupom.getValorDesconto() != null && cupom.getValorDesconto() != 0.0) {
            desconto = cupom.getValorDesconto();
        }

        this.aplicarDescontoNoPedido(pedido, desconto);
        cupom.setQuantidadeMaximaUso(cupom.getQuantidadeMaximaUso() - 1);
        this.update(cupom.getId(), cupom);
    }

}
