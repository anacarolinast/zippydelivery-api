package br.com.zippydeliveryapi.model.dto.request;

import br.com.zippydeliveryapi.model.CategoriaProduto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProdutoRequest {

  private String descricao;

  @Column(name = "empresa_id")
  private Long empresaId;

  public CategoriaProduto build() {
    return CategoriaProduto.builder()
        .descricao(descricao)
        .build();
  }
}
