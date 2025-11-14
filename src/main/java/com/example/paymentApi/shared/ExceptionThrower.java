package com.example.paymentApi.shared;

import com.example.paymentApi.shared.exception.GeneralAppException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ExceptionThrower {

    //1000 series - general
    //2000 series - input/parameter related
    //3000 series - user entity related
    //4000 series - all other business related

    //General
    private static final String INVALID_TOKEN_ERROR_CODE = "1000";
    private static final String INVALID_TOKEN_ERROR = "Invalid or expired token supplied";

    //Input/parameter
    private final String NULL_PARAMETER_ERROR_CODE = "2000";
    private final String NULL_PARAMETER_ERROR = "Please provide all the required information";
    private final String INVALID_EMAIL_PARAMETER_ERROR_CODE = "2001";
    private final String INVALID_EMAIL_PARAMETER_ERROR = "Invalid email format provided";
    private final String INVALID_INTEGER_PARAMETER_ERROR_CODE = "2002";
    private final String INVALID_INTEGER_PARAMETER_ERROR = "A number is expected, please provided a valid number";

    //User
    private final String USER_ALREADY_EXIST_ERROR_CODE = "3000";
    private final String USER_ALREADY_EXIST_ERROR = "User already exist";
    private final String USER_DOES_NOT_EXIST_ERROR_CODE = "3001";
    private final String USER_DOES_NOT_EXIST_ERROR = "User not found";
    private final String INVALID_LOGIN_CREDENTIALS_ERROR_CODE = "3002";
    private final String INVALID_LOGIN_CREDENTIALS_ERROR = "Invalid Login Credential supplied.";
    private final String USER_NOT_VERIFIED_ERROR_CODE = "3003";
    private final String USER_NOT_VERIFIED_ERROR = "Please verify your account to enable login";



    public void throwInvalidTokenException(String link) throws GeneralAppException{
        throw new GeneralAppException(HttpStatus.UNAUTHORIZED,
                INVALID_TOKEN_ERROR_CODE,
                INVALID_TOKEN_ERROR, link);
    }

    public void throwNullParameterException(String resourceUrl) throws GeneralAppException{
        throw new GeneralAppException(HttpStatus.UNAUTHORIZED,
                NULL_PARAMETER_ERROR_CODE,
                NULL_PARAMETER_ERROR, resourceUrl);
    }

    public void throwInvalidEmailAttributeException(String link) throws GeneralAppException {
        throw new GeneralAppException(HttpStatus.BAD_REQUEST,
                INVALID_EMAIL_PARAMETER_ERROR_CODE,
                INVALID_EMAIL_PARAMETER_ERROR,
                link);
    }

    public void throwInvalidIntegerAttributeException(String link) throws GeneralAppException {
        throw new GeneralAppException(HttpStatus.BAD_REQUEST,
                INVALID_INTEGER_PARAMETER_ERROR_CODE,
                INVALID_INTEGER_PARAMETER_ERROR,
                link);
    }

    public void throwUserAlreadyExistException(String link){
        throw new GeneralAppException(
                HttpStatus.BAD_REQUEST,
                USER_ALREADY_EXIST_ERROR_CODE,
                USER_ALREADY_EXIST_ERROR,
                link
        );
    }
    public GeneralAppException throwUserNotFoundExistException(String link){
        throw new GeneralAppException(
                HttpStatus.BAD_REQUEST,
                USER_DOES_NOT_EXIST_ERROR_CODE,
                USER_DOES_NOT_EXIST_ERROR,
                link
        );
    }

    public void throwInvalidLoginException(String link){
        throw new GeneralAppException(
                HttpStatus.BAD_REQUEST,
                INVALID_LOGIN_CREDENTIALS_ERROR_CODE,
                INVALID_LOGIN_CREDENTIALS_ERROR,
                link
        );
    }

    public void throwUserNotVerifiedException(String link){
        throw new GeneralAppException(
                HttpStatus.BAD_REQUEST,
                USER_NOT_VERIFIED_ERROR_CODE,
                USER_NOT_VERIFIED_ERROR,
                link
        );
    }

}
