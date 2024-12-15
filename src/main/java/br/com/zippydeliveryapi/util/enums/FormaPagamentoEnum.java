package br.com.zippydeliveryapi.util.enums;

public enum FormaPagamentoEnum {
    DINHEIRO(1, "Dinheiro"),
    CARTAO_CREDITO(2, "Cartão de Crédito"),
    CARTAO_DEBITO(3, "Cartão de Débito"),
    PIX(4, "PIX"),
    VALE_ALIMENTACAO(5, "Vale Alimentação"),
    OUTRAS(6, "Outras");

    private final int codigo;
    private final String descricao;

    FormaPagamentoEnum(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    // Método para obter um enum pelo código
    public static FormaPagamentoEnum fromCodigo(int codigo) {
        for (FormaPagamentoEnum forma : FormaPagamentoEnum.values()) {
            if (forma.getCodigo() == codigo) {
                return forma;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }

    // Método para obter um enum pela descrição
    public static FormaPagamentoEnum fromDescricao(String descricao) {
        for (FormaPagamentoEnum forma : FormaPagamentoEnum.values()) {
            if (forma.getDescricao().equalsIgnoreCase(descricao)) {
                return forma;
            }
        }
        throw new IllegalArgumentException("Descrição inválida: " + descricao);
    }
}
