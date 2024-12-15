package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.CupomDescontoRequest;
import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "CupomDesconto")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CupomDesconto extends EntidadeNegocio {

    @Column(unique = true)
    private String codigo;

    private Double percentualDesconto;

    private Double valorDesconto;

    private Double valorMinimoPedidoPermitido;

    private Integer quantidadeMaximaUso;

    private LocalDate inicioVigencia;

    private LocalDate fimVigencia;

    public static CupomDesconto fromRequest(CupomDescontoRequest request) {
        return CupomDesconto.builder()
                .codigo(request.getCodigo())
                .percentualDesconto(request.getPercentualDesconto())
                .valorDesconto(request.getValorDesconto())
                .valorMinimoPedidoPermitido(request.getValorMinimoPedidoPermitido())
                .quantidadeMaximaUso(request.getQuantidadeMaximaUso())
                .inicioVigencia(request.getInicioVigencia())
                .fimVigencia(request.getFimVigencia())
                .build();
    }
}
