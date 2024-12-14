package br.com.zippydeliveryapi.model.dto.request;

import br.com.zippydeliveryapi.model.CategoriaEmpresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaEmpresaRequest {

    private String descricao;

    public CategoriaEmpresa build() {
        return CategoriaEmpresa.builder()
                .descricao(descricao)
                .build();
    }
}
