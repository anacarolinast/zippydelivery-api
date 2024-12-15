package br.com.zippydeliveryapi.model.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import br.com.zippydeliveryapi.model.Cliente;
import br.com.zippydeliveryapi.model.Endereco;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

   @NotBlank(message = "O Nome é de preenchimento obrigatório")
   @Length(max = 100, message = "O Nome deverá ter no máximo {max} caracteres")
   private String nome;

   @NotBlank(message = "O CPF é de preenchimento obrigatório")
   @CPF(message = "O CPF informado é inválido")
   private String cpf;

   @NotBlank(message = "O Email é de preenchimento obrigatório")
   @Email(message = "O Email informado é inválido")
   private String email;

   @NotBlank(message = "A senha é de preenchimento obrigatório")
   private String senha;

   private List<Long> enderecosID;

   public Cliente build() {
      return Cliente.builder()
              .nome(nome)
              .email(email)
              .senha(senha)
              .cpf(cpf)
              .build();
   }

    public static ClienteRequest fromEntity(Cliente cliente) {
        ClienteRequest request = new ClienteRequest();
        request.setNome(cliente.getNome());
        request.setCpf(cliente.getCpf());
        request.setEmail(cliente.getEmail());
        request.setSenha(cliente.getSenha());

        if (cliente.getEnderecos() != null) {
            List<Long> enderecosIds = cliente.getEnderecos().stream()
                    .map(Endereco::getId)
                    .collect(Collectors.toList());
            request.setEnderecosID(enderecosIds);
        } else {
            request.setEnderecosID(new ArrayList<>());
        }
        return request;
    }
}
