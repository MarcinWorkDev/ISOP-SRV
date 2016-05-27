package sopi.module.person.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sopi.module.person.repository.ProfileRepository;
import sopi.module.person.repository.UserRepository;

@Component
public class ProfileModel {

	@Autowired ProfileRepository profileRepo;
	@Autowired UserRepository userRepo;
	
	public List<Profile> getProfiles(){
		return profileRepo.findAll();
	}
	
	public List<Profile> getProfiles(String type){
		return profileRepo.findAllByType(type);
	}
	
	public List<Profile> getProfileWithoutUser(){
		List<Profile> profiles = profileRepo.findAll();
		List<Profile> profiles2 = new ArrayList<>();		
		
		for (Profile profile : profiles) {
			
			boolean emailStatus;
			if (profile.getEmail() == null || profile.getEmail().length() < 3) {
				emailStatus = false;
			} else {
				emailStatus = true;
			}
			
			if (profile.getUser() == null && emailStatus == true) {
				profiles2.add(profile);
			}
		}
		
		return profiles2;
	}
	
	public void deleteProfile(Long id){
		
		if (userRepo.findOne(id) != null){
			userRepo.delete(id);
		}
		
		if (profileRepo.findOne(id) != null){
			profileRepo.delete(id);
		}
	}
	
	public Profile getProfile(String pesel){
		return profileRepo.getByPesel(pesel);
	}
	
	public void saveNew(Profile profile){
		profileRepo.save(profile);
	}
	
	public void save(Profile profile){
		
		Profile profile2 = profileRepo.findOne(profile.getProfileId());
		profile.setCreatedAt(profile2.getCreatedAt());
		profile.setCreatedBy(profile2.getCreatedBy());
		
		profileRepo.save(profile);
	}
}
