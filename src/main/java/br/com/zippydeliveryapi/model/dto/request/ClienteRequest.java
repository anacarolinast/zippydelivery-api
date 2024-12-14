package br.com.zippydeliveryapi.model.dto.request;

import java.util.List;
import br.com.zippydeliveryapi.model.Cliente;
import br.com.zippydeliveryapi.model.Usuario;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
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

    private String[] enderecos;

   public Cliente build() {
      return Cliente.builder()
              .nome(nome)
              .email(email)
              .senha(senha)
              .cpf(cpf)
              .build();
   }

  public Usuario buildUsuario() {
	
	return Usuario.builder()
		.username(email)
		.password(senha)
		.roles(List.of(Usuario.ROLE_CLIENTE))
		.build();
    }
}
