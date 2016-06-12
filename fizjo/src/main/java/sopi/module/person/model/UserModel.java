package sopi.module.person.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import sopi.module.auth.model.base.UserPasswordHelper;
import sopi.module.auth.security.AuthUtils;
import sopi.module.auth.security.TokenUtils;
import sopi.module.email.model.EmailModel;
import sopi.module.person.repository.RoleRepository;
import sopi.module.person.repository.UserRepository;

@Component
public class UserModel {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired UserRepository userRepo;
	@Autowired RoleRepository roleRepo;
	@Autowired AuthUtils auth;
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired EmailModel emailModel;
	
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
		
		user.setCreatedBy(auth.getLogged().getUsername());
		user.setCreatedAt(new DateTime(new Date()));
		user.setLastPasswordChangeAt(DateTime.parse("1900-01-01"));
		
		UserPasswordHelper passHelper = new UserPasswordHelper(passwordEncoder);
		passHelper.newPassword();
		String temporaryPassword = passHelper.getEncodedPassword();
		user.setPassword(temporaryPassword);
		
		userRepo.save(user);
		
		String emailTo = user.getProfile().getEmail();
		String emailSubject = "Nowe konto użytkownika w systemie ISOP.";		
		String emailTemplateName = "mail/createUser";
		Map<String, Object> emailTemplateVars = new HashMap<String, Object>();
		emailTemplateVars.put("user", user);
		emailTemplateVars.put("username", user.getUsername());
		emailTemplateVars.put("password", passHelper.getPassword());
		
		emailModel.sendEmail(emailTo, emailSubject, emailTemplateName, emailTemplateVars, true);
	}
	
	public String changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword){
		User user = userRepo.findOne(userId);
		
		UserPasswordHelper passHelper = new UserPasswordHelper(passwordEncoder,user.getPassword(),oldPassword,newPassword,confirmPassword);
		if (passHelper.changePassword()) {
			userRepo.changePassword(passHelper.getEncodedPassword(), userId);
			return "Hasło zostało zmienione";
		} else {
			return passHelper.getError();
		}
	}
	
	public String changePassword(String username, String oldPassword, String newPassword, String confirmPassword){
		User user = userRepo.getByUsername(username);
		
		UserPasswordHelper passHelper = new UserPasswordHelper(passwordEncoder,user.getPassword(),oldPassword,newPassword,confirmPassword);
		if (passHelper.changePassword()) {
			userRepo.changePassword(passHelper.getEncodedPassword(), user.userId);
			return "Hasło zostało zmienione";
		} else {
			return passHelper.getError();
		}
	}
	
	public void resetPassword(Long id){
		
		User user = userRepo.findOne(id);
		
		user.setLastPasswordChangeAt(DateTime.parse("1900-01-01"));
		
		UserPasswordHelper passHelper = new UserPasswordHelper(passwordEncoder);
		passHelper.newPassword();
		String temporaryPassword = passHelper.getEncodedPassword();
		user.setPassword(temporaryPassword);
		
		userRepo.save(user);
		
		String emailTo = user.getProfile().getEmail();
		String emailSubject = "Resetowanie hasła użytkownika w systemie ISOP.";		
		String emailTemplateName = "mail/resetPassword";
		Map<String, Object> emailTemplateVars = new HashMap<String, Object>();
		emailTemplateVars.put("user", user);
		emailTemplateVars.put("username", user.getUsername());
		emailTemplateVars.put("password", passHelper.getPassword());
		
		emailModel.sendEmail(emailTo, emailSubject, emailTemplateName, emailTemplateVars, true);
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
