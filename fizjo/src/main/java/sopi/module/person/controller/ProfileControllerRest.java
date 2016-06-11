package sopi.module.person.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.auth.security.AuthUtils;
import sopi.module.person.model.Profile;
import sopi.module.person.model.ProfileModel;
import sopi.rest.StatusHelper;

@RestController
@RequestMapping(value="/api/module/profile")
public class ProfileControllerRest {

	@Autowired ProfileModel profileModel;
	@Autowired AuthUtils auth;
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public ResponseEntity<?> list(HttpServletRequest request){
		List<Profile> list = new ArrayList<>();
		
		if (auth.checkRoles("ADMIN,PROFILE")){
			list = profileModel.getProfiles();
			return ResponseEntity.ok(list);
		}
		
		if (auth.checkRoles("T_PRACOWNIK")){
			list.addAll(profileModel.getProfiles("PACJENT"));
		}
		
		if (list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		} else {
			return ResponseEntity.ok(list);
		}
	}
	
	@RequestMapping(value="/getPracownik", method=RequestMethod.GET)
	public ResponseEntity<?> listPracownik(HttpServletRequest request){
		
		if (auth.checkRoles("ADMIN,PROFILE")){
			return ResponseEntity.ok(profileModel.getProfiles("PRACOWNIK"));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
	}
	
	@RequestMapping(value="/getPacjent", method=RequestMethod.GET)
	public ResponseEntity<?> listPacjent(HttpServletRequest request){
		
		if (auth.checkRoles("ADMIN,PROFILE,T_PRACOWNIK")){
			return ResponseEntity.ok(profileModel.getProfiles("PACJENT"));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
	}
	
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> get(@PathVariable Long id, HttpServletRequest request) {
		
		Profile profile = profileModel.getProfile(id);
		
		if (!auth.checkRoles("ADMIN,PROFILE,T_PRACOWNIK")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
		
		return ResponseEntity.ok(profileModel.getProfile(id));
	}
	
	@RequestMapping(value="/set/{id}", method=RequestMethod.PUT)
	public ResponseEntity<?> set(@PathVariable Long id, @Valid @RequestBody Profile profile, HttpServletRequest request) {

		if (!auth.checkRoles("ADMIN,PROFILE")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
		
		try {
			profileModel.save(profile);
			return ResponseEntity.ok(new StatusHelper(true, "Rekord został zaktualizowany.", profile));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), profile));
		}
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public ResponseEntity<?> add(@Valid @RequestBody Profile profile, HttpServletRequest request) {

		if (!auth.checkRoles("ADMIN,PROFILE")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}

		if (profileModel.getProfile(profile.getPesel()) != null){
			return ResponseEntity.badRequest().body(new StatusHelper(false, "W systemie istnieje już profil z podanym numerem PESEL.",profile));
		} else {
			try {
				profile.setCreatedAt(new DateTime(new Date()));
				profile.setCreatedBy("ADMIN_DEV");
				profileModel.saveNew(profile);
				return ResponseEntity.ok(new StatusHelper(true, "Rekord został dodany.", profile));
			} catch (Exception e) {
				return ResponseEntity.badRequest().body(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), profile));
			}
		}
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request){
		
		Profile profile = profileModel.getProfile(id);
		
		if (!auth.checkRoles("ADMIN,PROFILE")){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
		
		try {
			profileModel.deleteProfile(id);
			return ResponseEntity.ok(new StatusHelper(true,"Profil został usunięty", id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StatusHelper(false,"Wystąpił błąd: " + e.getMessage(), id));
		}
	}
}
