package br.com.zippydeliveryapi.model.dto.request;

import br.com.zippydeliveryapi.util.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaStatusRequest {

    private StatusEnum status;

}
