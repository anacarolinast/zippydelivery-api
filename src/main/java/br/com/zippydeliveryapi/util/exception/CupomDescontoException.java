package br.com.zippydeliveryapi.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class CupomDescontoException extends RuntimeException {

    public static final String MESSAGE_INVALID_DATE = "Data de vigência inválida, verifique se o Fim da vigência é posterior ao Início.";

    public CupomDescontoException(String message) {
        super(String.format(message));
    }

}
