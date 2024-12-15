package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.ProdutoRequest;
import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Produto")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto extends EntidadeNegocio {

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaProduto categoria;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false)
    private String imagem;

    private String descricao;

    @Column(nullable = false)
    private Double preco;

    private Boolean disponibilidade;


    public static Produto fromRequest(ProdutoRequest request) {
        return Produto.builder()
                .titulo(request.getTitulo())
                .imagem(request.getImagem())
                .descricao(request.getDescricao())
                .preco(request.getPreco())
                .disponibilidade(request.getDisponibilidade())
                .build();
    }
}
