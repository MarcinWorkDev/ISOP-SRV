package sopi.module.auth.model.factory;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import sopi.module.auth.model.security.CerberusUser;
import sopi.module.person.model.Role;
import sopi.module.person.model.User;

public class CerberusUserFactory {

  public static CerberusUser create(User user) {

	  Collection<GrantedAuthority> authorities = new ArrayList<>();
	  	  
	  if (user.getProfile().getType() != "PACJENT"){
		  for (Role role : user.getRoles()) {
			  authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
		  }
	  }
	  
	  switch (user.getProfile().getType()){
	  case "PACJENT":
		  authorities.add(new SimpleGrantedAuthority("ROLE_T_PACJENT"));
		  break;
	  case "PRACOWNIK":
		  authorities.add(new SimpleGrantedAuthority("ROLE_T_PRACOWNIK"));
		  break;
	  case "USER":
		  authorities.add(new SimpleGrantedAuthority("ROLE_T_USER"));
		  break;
	  }
	  
      return new CerberusUser(
		user.getUserId(),
		user.getUsername(),
		user.getPassword(),
		user.getLastPasswordChangeAt().toDate(),
		authorities
    );
  }

}
