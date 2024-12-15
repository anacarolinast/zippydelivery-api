package br.com.zippydeliveryapi.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.zippydeliveryapi.model.dto.request.PedidoRequest;
import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pedido")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido extends EntidadeNegocio {

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "cupom_id")
    private CupomDesconto cupomDesconto;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<ItensPedido> itensPedido;

    private LocalDateTime dataHora;

    private int formaPagamento;

    private int statusPedido;

    private int statusPagamento;

    private Double valorTotal;

    private Double taxaEntrega;

    private Endereco enderecoEntrega;

    public static Pedido fromRequest(PedidoRequest request) {
        return Pedido.builder()
                .formaPagamento(request.getFormaPagamento().getCodigo())
                .statusPedido(request.getStatusPedido().getCodigo())
                .statusPagamento(StatusEnum.PENDENTE.getCodigo())
                .build();
    }

}
