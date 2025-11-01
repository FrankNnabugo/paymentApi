package com.example.paymentApi.shared;

import com.example.paymentApi.shared.exception.GeneralAppException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ExceptionThrower {
    private static final String INVALID_TOKEN_ERROR_CODE = "400";
    private static final String INVALID_TOKEN_ERROR = "Invalid or expired token supplied";

    public void throwInvalidTokenException(String link) throws GeneralAppException{
        throw new GeneralAppException(HttpStatus.UNAUTHORIZED,
                INVALID_TOKEN_ERROR_CODE,
                INVALID_TOKEN_ERROR, link);
    }

}
