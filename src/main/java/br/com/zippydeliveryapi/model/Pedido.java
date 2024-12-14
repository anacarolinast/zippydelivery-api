package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import java.time.LocalDateTime;
import java.util.List;

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

    private String formaPagamento;

    private StatusEnum statusPedido;

    private StatusEnum statusPagamento;

    private Double valorTotal;

    private Double taxaEntrega;

    private Endereco enderecoEntrega;

}
