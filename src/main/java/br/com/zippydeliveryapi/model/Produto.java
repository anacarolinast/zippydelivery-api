package br.com.zippydeliveryapi.model;

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

}
