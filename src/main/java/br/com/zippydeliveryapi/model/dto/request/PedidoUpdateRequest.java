package br.com.zippydeliveryapi.model.dto.request;

import br.com.zippydeliveryapi.util.enums.FormaPagamentoEnum;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoUpdateRequest {

    private FormaPagamentoEnum formaPagamento;

    private StatusEnum statusPedido;

    private StatusEnum statusPagamento;

    private Long enderecoEntregaId;

}
