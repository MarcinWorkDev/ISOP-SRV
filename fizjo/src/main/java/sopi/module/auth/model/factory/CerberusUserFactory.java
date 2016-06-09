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
	  
	  for (Role role : user.getRoles()) {
		  authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
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
