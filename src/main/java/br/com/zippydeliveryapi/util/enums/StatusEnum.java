package br.com.zippydeliveryapi.util.enums;

public enum StatusEnum {

    ATIVO(1, "Ativo"),
    INATIVO(0, "Inativo"),
    PENDENTE(2, "Pendente"),
    AGUARDANDO_APROVACAO(3, "Aguardando Aprovação"),
    CANCELADO(5, "Cancelado"),
    ESTORNADO(4, "Estornado");

    private final int codigo;
    private final String descricao;

    StatusEnum(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    // Metodo para obter um enum pelo código
    public static StatusEnum fromCodigo(int codigo) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }

    // Metodo para obter um enum pela descrição
    public static StatusEnum fromDescricao(String descricao) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getDescricao().equalsIgnoreCase(descricao)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Descrição inválida: " + descricao);
    }
}
