package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.ClienteRequest;
import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "Cliente")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends EntidadeNegocio {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    private String nome;

    private String cpf;

    private String email;

    private String senha;

    private List<Endereco> enderecos;


    public static Cliente fromRequest(ClienteRequest request) {
        return Cliente.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .email(request.getEmail())
                .build();
    }
}
