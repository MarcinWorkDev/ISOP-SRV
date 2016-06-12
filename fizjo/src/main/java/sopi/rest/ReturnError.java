package sopi.rest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ReturnError {
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();

	private int internalCode;
	
    private final String errorMessage;

    public ReturnError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public ReturnError(String errorMessage, int internalCode){
    	this.errorMessage = errorMessage;
    	this.internalCode = internalCode;
    }

    public void addValidationError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
    public int getInternalCode(){
    	return internalCode;
    }
}
