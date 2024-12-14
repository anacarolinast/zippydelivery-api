package br.com.zippydeliveryapi.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItensPedidoRequest {

    private Long produtoId;

    private Long pedidoId;

    private Integer qtdProduto;

    private Double valorUnitario;
 
}

