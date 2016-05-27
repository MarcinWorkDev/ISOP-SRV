package sopi.module.person.controller;

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
import sopi.rest.StatusHelper;

@RestController
@RequestMapping(value="/api/module/profile")
public class ProfileControllerRest {

	@Autowired ProfileModel profileModel;
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public List<Profile> list(){
		return profileModel.getProfiles();
	}
	
	@RequestMapping(value="/getPracownik", method=RequestMethod.GET)
	public List<Profile> listPracownik(){
		return profileModel.getProfiles("PRACOWNIK");
	}
	
	@RequestMapping(value="/getPacjent", method=RequestMethod.GET)
	public List<Profile> listPacjent(){
		return profileModel.getProfiles("PACJENT");
	}
	
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	public Profile get(@PathVariable Long id) {
		return null;
	}
	
	@RequestMapping(value="/set/{id}", method=RequestMethod.PUT)
	public StatusHelper set(@PathVariable Long id, @Valid @RequestBody Profile profile) {

		try {
			profileModel.save(profile);
			return new StatusHelper(true, "Rekord został zaktualizowany.", profile);
		} catch (Exception e) {
			return new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), profile);
		}
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public StatusHelper add(@Valid @RequestBody Profile profile) {
		if (profileModel.getProfile(profile.getPesel()) != null){
			return new StatusHelper(false, "W systemie istnieje już profil z podanym numerem PESEL.",profile);
		} else {
			try {
				profile.setCreatedAt(new DateTime(new Date()));
				profile.setCreatedBy("ADMIN_DEV");
				profileModel.saveNew(profile);
				return new StatusHelper(true, "Rekord został dodany.", profile);
			} catch (Exception e) {
				return new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), profile);
			}
		}
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public StatusHelper delete(@PathVariable Long id){
		try {
			profileModel.deleteProfile(id);
			return new StatusHelper(true,"Profil został usunięty", id);
		} catch (Exception e) {
			return new StatusHelper(false,"Wystąpił błąd: " + e.getMessage(), id);
		}
	}
}
