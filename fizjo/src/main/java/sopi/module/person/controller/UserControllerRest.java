package sopi.module.person.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.auth.security.AuthUtils;
import sopi.module.person.model.Profile;
import sopi.module.person.model.ProfileModel;
import sopi.module.person.model.Role;
import sopi.module.person.model.RoleModel;
import sopi.module.person.model.User;
import sopi.module.person.model.UserChangePassword;
import sopi.module.person.model.UserModel;
import sopi.rest.StatusHelper;

@RestController
@RequestMapping("/api/module/user")
public class UserControllerRest {

	@Autowired UserModel userModel;
	@Autowired RoleModel roleModel;
	@Autowired ProfileModel profileModel;
	@Autowired AuthUtils auth;
	
	@RequestMapping(value="*", method=RequestMethod.OPTIONS)
	public boolean options() {
		return true;
	}
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public ResponseEntity<?> list(HttpServletRequest request) {
		List<User> list = new ArrayList<>();
		
		if (auth.checkRoles("ADMIN,USER")){
			list.addAll(userModel.getUsers());
		}
		
		if (list.isEmpty()){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		} else {
			return ResponseEntity.ok(list);
		}
		
	}
	
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> get(@PathVariable Long id, HttpServletRequest request) {
		
		if (!auth.checkRoles("ADMIN,USER")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
		
		return ResponseEntity.ok(userModel.getUser(id));
	}
	
	@RequestMapping(value="/getRoles/{id}", method=RequestMethod.GET)
	public List<Role> getUserRoles(@PathVariable Long id, HttpServletRequest request) {
		return userModel.getUserRoles(id);
	}
	
	@RequestMapping(value="/getRoles", method=RequestMethod.GET)
	public List<Role> getRoles(HttpServletRequest request) {
		return roleModel.getRoles();
	}
	
	@RequestMapping(value="/getAvailRoles/{id}", method=RequestMethod.GET)
	public List<Role> getAvailRoles(@PathVariable Long id, HttpServletRequest request) {
		List<Role> roles = roleModel.getRoles();
		List<Role> userRoles = userModel.getUserRoles(id);
		
		roles.removeAll(userRoles);
		
		if (!auth.checkRoles("ADMIN")){
			Role admin = roleModel.getRole("ADMIN");
			roles.remove(admin);
		}
		
		return roles;
	}
	
	@RequestMapping(value="/setUserRole/{id}/{role}", method=RequestMethod.POST)
	public ResponseEntity<?> setUserRole(@PathVariable Long id, @PathVariable String role, HttpServletRequest request) {
		
		if (!auth.checkRoles("ADMIN,USER")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}		
		
		List<Object> param = new ArrayList<Object>();
		param.add(id);
		param.add(role);
		
		try {
			userModel.setUserRole(id, role);
			return ResponseEntity.ok(new StatusHelper(true, "Rola została dodana", param));
		} catch (Exception e) {
			return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), param));
		}
	}
	
	@RequestMapping(value="/deleteUserRole/{id}/{role}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteUserRole(@PathVariable Long id, @PathVariable String role, HttpServletRequest request){
		
		if (!auth.checkRoles("ADMIN,USER")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}	
		
		try {
			boolean status = userModel.deleteUserRole(id, role);
			return ResponseEntity.ok(new StatusHelper(true, "Rola została usunięta.", status));
		} catch (Exception e) {
			return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd:" + e.getMessage(), null));
		}
	}
	
	@RequestMapping(value="/set/{id}", method=RequestMethod.PUT)
	public ResponseEntity<?> set(@PathVariable Long id, @Valid @RequestBody User user, HttpServletRequest request) {
		
		User checkUser = userModel.getUser(user.getUsername());
		
		if (!auth.checkRoles("ADMIN,USER")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
		
		if (id != user.getUserId()) {
			return ResponseEntity.ok(new StatusHelper(false, "Niezgodne numery Id! " + id.toString() + " != " + user.getUserId().toString(), user));
		}
				
		if (checkUser != null && checkUser.getUserId() != user.getUserId()) {
			return ResponseEntity.ok(new StatusHelper(false, "Podana nazwa użytkownika jest niedostępna.", user));
		}
		
		try {
			userModel.save(user);
			return ResponseEntity.ok(new StatusHelper(true, "Rekord został zaktualizowany.", user));
		} catch (Exception e) {
			return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: " + e.getLocalizedMessage(), user));
		}
		
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public ResponseEntity<?> add(@Valid @RequestBody User user, HttpServletRequest request) {
		
		if (!auth.checkRoles("ADMIN,USER")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
		
		if (userModel.getUser(user.getUsername()) != null){
			return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: Wybrana nazwa użytkownika jest niedostępna.",user));
		}
		
		if (userModel.getUser(user.getUserId()) == null) {
			try {				
				userModel.saveNew(user);
				return ResponseEntity.ok(new StatusHelper(true, "Rekord został dodany.", user));
			} catch (Exception e) {
				return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), user));
			}
		} else {
			return ResponseEntity.ok(new StatusHelper(false, "Wystąpił błąd: Konto użytkownika dla wybranego profilu już istnieje.",user));
		}
	}
	
	@RequestMapping(value="/getAvailProfiles", method=RequestMethod.GET)
	public ResponseEntity<?> getProfiles(HttpServletRequest request) {
		
		List<Profile> profiles = profileModel.getProfileWithoutUser();		
		
		if (auth.checkRoles("ADMIN,USER")){
			return ResponseEntity.ok(profiles);
		} else{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		} 		
	}
	
	@RequestMapping(value="/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpServletRequest request){
		
		User user = userModel.getUser(id);
		
		if (!auth.checkRoles("ADMIN,USER")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
		
		try {
			userModel.deleteUser(id);
			return ResponseEntity.ok(new StatusHelper(true,"Użytkownik został usunięty", id));
		} catch (Exception e) {
			return ResponseEntity.ok(new StatusHelper(false,"Wystąpił błąd: " + e.getMessage(), id));
		}
	}
	
	@RequestMapping(value="/resetPassword/{id}", method=RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@PathVariable Long id){
		if (!auth.checkRoles("ADMIN,USER")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
		
		try {
			userModel.resetPassword(id);
			return ResponseEntity.ok(new StatusHelper(true,"Hasło zostało zresetowane", id));
		} catch (Exception e){
			return ResponseEntity.ok(new StatusHelper(false,"Wystąpił błąd: " + e.getMessage(), id));
		}
	}
	
	@RequestMapping(value="/changePassword", method=RequestMethod.POST)
	public ResponseEntity<?> changePassword(@RequestBody UserChangePassword userChangePassword){
		try {
			String result = userModel.changePassword(
					userChangePassword.getUserId(), 
					userChangePassword.getOldPassword(), 
					userChangePassword.getNewPassword(), 
					userChangePassword.getConfirmPassword());
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Wystąpił błąd podczas zmiany hasła: " + e.getMessage());
		}
	}
	
	@RequestMapping(value="/getLogged")
	public User getLogged(ServletRequest request, ServletResponse response){
		HttpServletRequest httpRequest = (HttpServletRequest) request;
	    String authToken = httpRequest.getHeader("X-Auth-Token");
	    
	    User user = userModel.getUserFromToken(authToken);
	    return user;
	}
	@RequestMapping(value="/getLoggedRoles")
	public ResponseEntity<?> getLoggedRoles(ServletRequest request, ServletResponse response){
		HttpServletRequest httpRequest = (HttpServletRequest) request;
	    String authToken = httpRequest.getHeader("X-Auth-Token");
	    
	    User user = userModel.getUserFromToken(authToken);
	    
	    List<String> roles = new ArrayList<>();
	    
	    for (GrantedAuthority role : SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
	    	roles.add(role.getAuthority());
	    }
	    	    
	    return ResponseEntity.ok(roles);
	}
}
