package sopi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sopi.module.person.model.User;
import sopi.module.person.repository.UserRepository;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.getByUsername(userName);
		
		if (user != null) {
			return (UserDetails) user;
		}
		throw new UsernameNotFoundException("U¿ytkownik nie zosta³ znaleziony.");
	}
}
