package br.com.zippydeliveryapi.model.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import br.com.zippydeliveryapi.util.enums.StatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    @NotNull
    private List<ItensPedidoRequest> itens;

    private Long clienteId;

    private Long empresaId;

    private String codigoCupom;

    private LocalDateTime dataHora;

    private String formaPagamento;

    private StatusEnum statusPedido;

    private StatusEnum statusPagamento;

    private Double taxaEntrega;

}
