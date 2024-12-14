package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import java.io.Serializable;

@Entity
@Table(name = "Endereco")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco extends EntidadeNegocio implements Serializable {

    private String logradouro;

    private String numero;

    private String bairro;

    private String cidade;

    private String estado;

    @Length(max = 10, message = "O CEP deverá ter no máximo {max} caracteres")
    private String cep;

    private String complemento;

}
