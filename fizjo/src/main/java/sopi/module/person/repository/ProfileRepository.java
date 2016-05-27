package sopi.module.person.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sopi.module.person.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>{

	Profile getByPesel(String pesel);
	List<Profile> findAllByType(String type);
}
