package sopi.module.auth.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import sopi.rest.ReturnError;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
    //httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Ooo nieee... :( Brak dostępu!");
    httpServletResponse.setContentType("application/json");
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    
    ObjectMapper obj = new ObjectMapper();
    
    ReturnError returnError = new ReturnError("ACCESS_DENIED");
    returnError.addValidationError("Brak dostępu.");
    
    obj.writeValue(httpServletResponse.getOutputStream(), returnError);
  }

}
