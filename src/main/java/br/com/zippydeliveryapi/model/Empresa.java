package br.com.zippydeliveryapi.model;

import java.util.Set;
import br.com.zippydeliveryapi.model.dto.request.EmpresaRequest;
import br.com.zippydeliveryapi.util.entity.EntidadeNegocio;
import br.com.zippydeliveryapi.util.enums.FormaPagamentoEnum;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "Empresa")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Empresa extends EntidadeNegocio {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaEmpresa categoria;

    @ElementCollection(targetClass = FormaPagamentoEnum.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "empresa_formas_pagamento", joinColumns = @JoinColumn(name = "empresa_id"))
    @Column(name = "forma_pagamento")
    @Enumerated(EnumType.STRING)
    private Set<FormaPagamentoEnum> formasPagamento;

    private String nome;

    private String cnpj;

    @Email
    private String email;

    private Integer tempoEntrega;

    private Double taxaFrete;

    private String telefone;

    private String imgPerfil;

    private String imgCapa;

    private int status;

    private String senha;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    public static Empresa fromRequest(EmpresaRequest request) {
        return Empresa.builder()
                .formasPagamento(request.getFormasPagamento())
                .nome(request.getNome())
                .cnpj(request.getCnpj())
                .email(request.getEmail())
                .tempoEntrega(request.getTempoEntrega())
                .taxaFrete(request.getTaxaFrete())
                .telefone(request.getTelefone())
                .imgPerfil(request.getImgPerfil())
                .imgCapa(request.getImgCapa())
                .status(StatusEnum.PENDENTE.getCodigo())
                .build();
    }

}
