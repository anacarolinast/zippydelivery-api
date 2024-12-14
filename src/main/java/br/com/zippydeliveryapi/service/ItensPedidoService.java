package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.ItensPedido;
import br.com.zippydeliveryapi.repository.ItensPedidoRepository;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItensPedidoService {

    @Autowired
    private final ItensPedidoRepository repository;

    public ItensPedidoService(ItensPedidoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ItensPedido save(ItensPedido itensPedido) {
        itensPedido.setHabilitado(Boolean.TRUE);
        itensPedido.setValorTotal(itensPedido.getValorUnitario() * itensPedido.getQtdProduto());
        return this.repository.save(itensPedido);
    }

    public List<ItensPedido> findAll() {
        return this.repository.findAll();
    }

    public ItensPedido findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("ItensPedido", id));
    }

    @Transactional
    public void update(Long id, ItensPedido itensPedidoAlterado) {
        ItensPedido itensPedido = this.findById(id);
        itensPedido.setProduto(itensPedidoAlterado.getProduto());
        itensPedido.setPedido(itensPedidoAlterado.getPedido());
        itensPedido.setQtdProduto(itensPedidoAlterado.getQtdProduto());
        itensPedido.setValorUnitario(itensPedidoAlterado.getValorUnitario());
        itensPedido.setValorTotal(itensPedidoAlterado.getValorUnitario() * itensPedidoAlterado.getQtdProduto());
        this.repository.save(itensPedido);
    }

    @Transactional
    public void delete(Long id) {
        ItensPedido itensPedido = this.findById(id);
        itensPedido.setHabilitado(Boolean.FALSE);
        this.repository.save(itensPedido);
    }
}
