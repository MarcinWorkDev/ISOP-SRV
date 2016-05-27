package sopi.module.person.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.person.model.Profile;
import sopi.module.person.model.ProfileModel;
import sopi.module.person.model.Role;
import sopi.module.person.model.RoleModel;
import sopi.module.person.model.User;
import sopi.module.person.model.UserModel;
import sopi.rest.StatusHelper;

@RestController
@RequestMapping("/api/module/user")
public class UserControllerRest {

	@Autowired UserModel userModel;
	@Autowired RoleModel roleModel;
	@Autowired ProfileModel profileModel;
	
	@RequestMapping(value="*", method=RequestMethod.OPTIONS)
	public boolean options() {
		return true;
	}
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public List<User> list() {
		return userModel.getUsers();
	}
	
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	public User get(@PathVariable Long id) {
		return userModel.getUser(id);
	}
	
	@RequestMapping(value="/getRoles/{id}", method=RequestMethod.GET)
	public List<Role> getUserRoles(@PathVariable Long id) {
		return userModel.getUserRoles(id);
	}
	
	@RequestMapping(value="/getRoles", method=RequestMethod.GET)
	public List<Role> getRoles() {
		return roleModel.getRoles();
	}
	
	@RequestMapping(value="/getAvailRoles/{id}", method=RequestMethod.GET)
	public List<Role> getAvailRoles(@PathVariable Long id) {
		List<Role> roles = roleModel.getRoles();
		List<Role> userRoles = userModel.getUserRoles(id);
		
		roles.removeAll(userRoles);
		
		return roles;
	}
	
	@RequestMapping(value="/setUserRole/{id}/{role}", method=RequestMethod.POST)
	public StatusHelper setUserRole(@PathVariable Long id, @PathVariable String role) {
		
		List<Object> param = new ArrayList<Object>();
		param.add(id);
		param.add(role);
		
		try {
			userModel.setUserRole(id, role);
			return new StatusHelper(true, "Rola została dodana", param);
		} catch (Exception e) {
			return new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), param);
		}
	}
	
	@RequestMapping(value="/deleteUserRole/{id}/{role}", method=RequestMethod.DELETE)
	public StatusHelper deleteUserRole(@PathVariable Long id, @PathVariable String role){
		try {
			boolean status = userModel.deleteUserRole(id, role);
			return new StatusHelper(true, "Rola została usunięta.", status);
		} catch (Exception e) {
			return new StatusHelper(false, "Wystąpił błąd:" + e.getMessage(), null);
		}
	}
	
	@RequestMapping(value="/set/{id}", method=RequestMethod.PUT)
	public StatusHelper set(@PathVariable Long id, @Valid @RequestBody User user) {
		
		if (id != user.getUserId()) {
			return new StatusHelper(false, "Niezgodne numery Id! " + id.toString() + " != " + user.getUserId().toString(), user);
		}
		
		User checkUser = userModel.getUser(user.getUsername());
		
		if (checkUser != null && checkUser.getUserId() != user.getUserId()) {
			return new StatusHelper(false, "Podana nazwa użytkownika jest niedostępna.", user);
		}
		
		try {
			userModel.save(user);
			return new StatusHelper(true, "Rekord został zaktualizowany.", user);
		} catch (Exception e) {
			return new StatusHelper(false, "Wystąpił błąd: " + e.getLocalizedMessage(), user);
		}
		
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public StatusHelper add(@Valid @RequestBody User user) {
		
		if (userModel.getUser(user.getUsername()) != null){
			return new StatusHelper(false, "Wystąpił błąd: Wybrana nazwa użytkownika jest niedostępna.",user);
		}
		
		if (userModel.getUser(user.getUserId()) == null) {
			try {
				
				user.setCreatedBy("ADMIN_TEST");
				user.setCreatedAt(new DateTime(new Date()));
				user.setLastPasswordChangeAt(new DateTime(new Date()));
				user.setPassword("PASSWORD_TEST");
				
				userModel.saveNew(user);
				return new StatusHelper(true, "Rekord został dodany.", user);
			} catch (Exception e) {
				return new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), user);
			}
		} else {
			return new StatusHelper(false, "Wystąpił błąd: Konto użytkownika dla wybranego profilu już istnieje.",user);
		}
	}
	
	@RequestMapping(value="/getAvailProfiles", method=RequestMethod.GET)
	public List<Profile> getProfiles() {
		return profileModel.getProfileWithoutUser();
	}
	
	@RequestMapping(value="/delete/{id}")
	public StatusHelper deleteUser(@PathVariable Long id){
		try {
			userModel.deleteUser(id);
			return new StatusHelper(true,"Użytkownik został usunięty", id);
		} catch (Exception e) {
			return new StatusHelper(false,"Wystąpił błąd: " + e.getMessage(), id);
		}
	}
}
