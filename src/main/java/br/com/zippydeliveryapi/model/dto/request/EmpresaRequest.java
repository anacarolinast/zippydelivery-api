package br.com.zippydeliveryapi.model.dto.request;

import br.com.zippydeliveryapi.util.enums.FormaPagamentoEnum;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {

    @Length(max = 500, message = "O Nome deverá ter no máximo {max} caracteres")
    private String nome;

    @Length(max = 20, message = "CNPJ out of range, too long")
    private String cnpj;

    @Email
    private String email;

    private Long categoriaId;

    private EnderecoRequest endereco;

    private Integer tempoEntrega;

    private Double taxaFrete;

    private String telefone;

    private String imgPerfil;

    private String imgCapa;

    private StatusEnum status;

    private String senha;

    private Set<FormaPagamentoEnum> formasPagamento;

}
