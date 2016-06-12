package sopi.module.person.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.auth.security.AuthUtils;
import sopi.module.person.model.Profile;
import sopi.module.person.model.ProfileModel;
import sopi.module.person.model.UserModel;
import sopi.rest.StatusHelper;

@RestController
@RequestMapping(value="/api/module/myprofile")
public class MyProfileControllerRest {

	@Autowired ProfileModel profileModel;
	@Autowired UserModel userModel;
	@Autowired AuthUtils auth;
	
	@RequestMapping
	public ResponseEntity<?> getMyProfile(){
		Profile profile = auth.getLogged().getProfile();
		return ResponseEntity.ok(profile);
	}
	
	@RequestMapping(value="/set", method=RequestMethod.PUT)
	public ResponseEntity<?> saveProfile(@Valid @RequestBody Profile profile){
		Profile origin = profileModel.getProfile(profile.getProfileId());
		Long reqId = profile.getProfileId();
		Long logId = auth.getLogged().getProfile().getProfileId();
		
		if (reqId != logId){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS_DENIED");
		}
		
		origin.setEmail(profile.getEmail());
		origin.setKodPocztowy(profile.getKodPocztowy());
		origin.setMiejscowosc(profile.getMiejscowosc());
		origin.setMobile(profile.getMobile());
		origin.setNrDomu(profile.getNrDomu());
		origin.setNrMieszkania(profile.getNrMieszkania());
		origin.setNumerDodatkowy(profile.getNumerDodatkowy());
		origin.setUlica(profile.getUlica());
		
		try {
			profileModel.save(origin);
			return ResponseEntity.ok(new StatusHelper(true, "Rekord został zaktualizowany.", profile));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StatusHelper(false, "Wystąpił błąd: " + e.getMessage(), profile));
		}
	}
}
