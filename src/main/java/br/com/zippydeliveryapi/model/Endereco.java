package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.EnderecoRequest;
import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "Endereco")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco extends EntidadeNegocio {

    private String logradouro;

    private String numero;

    private String bairro;

    private String cidade;

    private String estado;

    @Length(max = 10, message = "O CEP deverá ter no máximo {max} caracteres")
    private String cep;

    private String complemento;

    public static Endereco fromRequest(EnderecoRequest request) {
        return Endereco.builder()
                .logradouro(request.getLogradouro())
                .numero(request.getNumero())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .cep(request.getCep())
                .complemento(request.getComplemento())
                .build();
    }
}
