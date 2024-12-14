package br.com.zippydeliveryapi.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardResponse {

    private Integer vendasTotais;
    private Double fatoramentoTotal;
    private Integer vendaHoje;
    private Double faturamentoMedio;

}
