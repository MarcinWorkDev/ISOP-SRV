package sopi.module.person.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sopi.module.person.repository.RoleRepository;

@Component
public class RoleModel {

	@Autowired RoleRepository roleRepo;
	
	public Role getRole(String role) {
		return roleRepo.findOne(role);
	}
	
	public List<Role> getRoles() {
		return roleRepo.findAll();
	}
}
