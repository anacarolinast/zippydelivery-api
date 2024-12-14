package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "CategoriaEmpresa")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaEmpresa extends EntidadeNegocio {

    private String descricao;

}
