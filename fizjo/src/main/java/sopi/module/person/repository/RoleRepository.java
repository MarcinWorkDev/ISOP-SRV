package sopi.module.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sopi.module.person.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{

	
}
