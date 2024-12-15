package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.CupomDesconto;
import br.com.zippydeliveryapi.model.dto.request.CupomDescontoRequest;
import br.com.zippydeliveryapi.repository.CupomDescontoRepository;
import br.com.zippydeliveryapi.util.exception.CupomDescontoException;
import br.com.zippydeliveryapi.model.Pedido;
import br.com.zippydeliveryapi.util.exception.ProdutoException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CupomDescontoService {

    @Autowired
    private final CupomDescontoRepository repository;

    public CupomDescontoService(CupomDescontoRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public CupomDesconto save(CupomDescontoRequest request) {
        Optional<CupomDesconto> cupom = this.repository.findByCodigo(request.getCodigo());
        if(cupom.isPresent()){
            throw new ProdutoException("Já existe um cupom de desconto ativo com esse código");
        }
        CupomDesconto cupomDesconto = CupomDesconto.fromRequest(request);
        this.validateDateRange(cupomDesconto);
        cupomDesconto.setHabilitado(Boolean.TRUE);
        return this.repository.save(cupomDesconto);
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
    public void update(Long id, CupomDescontoRequest request) {
        CupomDesconto cupomDesconto = this.findById(id);
        cupomDesconto.setCodigo(StringUtils.hasText(request.getCodigo()) ? request.getCodigo() : cupomDesconto.getCodigo());
        cupomDesconto.setPercentualDesconto((request.getPercentualDesconto() > 0) ? request.getPercentualDesconto() : cupomDesconto.getPercentualDesconto());
        cupomDesconto.setValorDesconto((request.getValorDesconto() >= 0) ? request.getValorDesconto() : cupomDesconto.getValorDesconto());
        cupomDesconto.setValorMinimoPedidoPermitido((request.getValorMinimoPedidoPermitido() > 0) ? request.getValorMinimoPedidoPermitido() : cupomDesconto.getValorMinimoPedidoPermitido());
        cupomDesconto.setQuantidadeMaximaUso((request.getQuantidadeMaximaUso() > 0) ? request.getQuantidadeMaximaUso() : cupomDesconto.getQuantidadeMaximaUso());
        cupomDesconto.setInicioVigencia(request.getInicioVigencia());
        cupomDesconto.setFimVigencia(request.getFimVigencia());
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
        this.update(cupom.getId(), CupomDescontoRequest.fromEntity(cupom));
    }

}
