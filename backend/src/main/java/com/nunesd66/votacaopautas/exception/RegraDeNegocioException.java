package com.nunesd66.votacaopautas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegraDeNegocioException extends Exception {

    public RegraDeNegocioException(String msg) {
        super(msg);
    }

}
