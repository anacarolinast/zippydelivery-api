package br.com.zippydeliveryapi.model.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorRequest {

    private String nome;

    @CPF
    private String cpf;

    @Email
    private String email;

    private String senha;

    private String dataNascimento;

    private String veiculo;

    private String placa;

    private String telefone;

    private int status;

}
