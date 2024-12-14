package br.com.zippydeliveryapi.model.dto.response;

import br.com.zippydeliveryapi.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriasProdutoEmpresaResponse {

    private String titulo;
    private List<Produto> produtos;

}
