package sopi.module.auth.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sopi.module.person.model.User;
import sopi.module.person.model.UserModel;

@Component
public class AuthUtils {

	@Autowired HttpServletRequest request;
	@Autowired UserModel userModel;
	
	public Boolean checkRoles(String rolesString){
		
		Boolean status = false;
		
		String[] roles = rolesString.split(",");
		
		for (String role : roles){
			if (request.isUserInRole("ROLE_" + role)){
				status = true;
			}
		}
		
		return status;
	}
	
	public User getLogged(){
		return userModel.getUser(request.getUserPrincipal().getName());
	}
}
