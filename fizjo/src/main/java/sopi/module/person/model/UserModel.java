package sopi.module.person.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sopi.module.auth.security.TokenUtils;
import sopi.module.person.repository.RoleRepository;
import sopi.module.person.repository.UserRepository;

@Component
public class UserModel {

	@Autowired UserRepository userRepo;
	@Autowired RoleRepository roleRepo;
	
	public List<User> getUsers(String type){
		return userRepo.findAllByProfileType(type);
	}
	
	public User getUser(Long userId) {		
		return userRepo.findOne(userId);
	}
	
	public User getUser(String username) {
		return userRepo.getByUsername(username);
	}
	
	public List<Role> getUserRoles(Long userId) {
		return userRepo.getOne(userId).getRoles();
	}
	
	public List<Role> getUserRoles(String username) {
		return userRepo.getByUsername(username).getRoles();
	}
	
	public boolean deleteUserRole(Long userId, String role) {
		Role roleItem = roleRepo.getOne(role);
		User user = userRepo.getOne(userId);
		
		boolean status = user.getRoles().remove(roleItem);
		
		userRepo.saveAndFlush(user);
		
		return status;
	}
	
	public void setUserRole(Long userId, String role) {
		Role roleItem = roleRepo.getOne(role);
		User user = userRepo.getOne(userId);
		
		user.getRoles().add(roleItem);
		
		userRepo.saveAndFlush(user);
	}
	
	public List<User> getUsers() {
		return userRepo.findAll();
	}
	
	public void save(User user) {
		
		user.setPassword(userRepo.getOne(user.getUserId()).getPassword());
		
		userRepo.save(user);
	}
	
	public void saveNew(User user) {
		userRepo.save(user);
	}
	
	public void deleteUser(Long id) {
		userRepo.delete(id);
	}
	
	public User getUserFromToken(String tokenString){
		TokenUtils token = new TokenUtils();
		User user = userRepo.getByUsername(token.getUsernameFromToken(tokenString));
		return user;
	}
	
}
