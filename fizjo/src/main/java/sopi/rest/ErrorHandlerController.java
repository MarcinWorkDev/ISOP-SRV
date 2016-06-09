package sopi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ErrorHandlerController {

	@ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ReturnError handleException(MethodArgumentNotValidException exception) {
        return createValidationError(exception);
    }
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ReturnError handleDenied(AuthenticationException exception) {
		return new ReturnError("ACCESS_DENIED");
	}

    private ReturnError createValidationError(MethodArgumentNotValidException e) {
        return ValidationErrorBuilder.fromBindingErrors(e.getBindingResult());
    }
}
