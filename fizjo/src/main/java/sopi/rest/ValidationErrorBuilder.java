package sopi.rest;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class ValidationErrorBuilder {
	public static ValidationError fromBindingErrors(Errors errors) {
        ValidationError error = new ValidationError("Błąd podczas walidacji. " + errors.getErrorCount() + " błąd(ów)");
        for (ObjectError objectError : errors.getAllErrors()) {
            error.addValidationError(objectError.getDefaultMessage());
        }
        return error;
    }
}
