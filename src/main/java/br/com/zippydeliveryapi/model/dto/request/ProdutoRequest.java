package br.com.zippydeliveryapi.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {

   private Long categoriaId;

   private Long empresaId;

   private String titulo;

   private String imagem;

   private String descricao;

   private Double preco;

   private Boolean disponibilidade;

}