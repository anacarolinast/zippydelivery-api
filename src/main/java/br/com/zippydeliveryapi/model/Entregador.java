package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "Entregador")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entregador extends EntidadeNegocio {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nome;

    @CPF
    private String cpf;

    @Email
    private String email;

    private String senha;

    private String dataNascimento;

    private String veiculo;

    @Column(nullable = false, unique = true)
    private String placa;

    private String telefone;

    private StatusEnum status;

}
