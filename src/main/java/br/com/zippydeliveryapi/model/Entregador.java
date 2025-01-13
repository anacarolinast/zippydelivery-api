package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.EmpresaRequest;
import br.com.zippydeliveryapi.model.dto.request.EntregadorRequest;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import org.hibernate.validator.constraints.br.CPF;

import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String cpf;

    @Email
    private String email;

    private String senha;

    private String dataNascimento;

    private String veiculo;

    @Column(nullable = false, unique = true)
    private String placa;

    private String telefone;

    private int status;

    public static Entregador fromRequest(EntregadorRequest request) {
        return Entregador.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .email(request.getEmail())
                .dataNascimento(request.getDataNascimento())
                .telefone(request.getTelefone())
                .veiculo(request.getVeiculo())
                .placa(request.getPlaca())
                .senha(request.getSenha())
                .status(StatusEnum.PENDENTE.getCodigo())
                .build();
    }
}
