package sopi.module.person.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sopi.module.person.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User getByUsername(String username);
	List<User> findAllByProfileType(String type);

}
