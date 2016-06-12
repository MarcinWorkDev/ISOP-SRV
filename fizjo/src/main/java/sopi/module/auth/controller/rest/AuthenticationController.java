package sopi.module.auth.controller.rest;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sopi.module.auth.model.json.request.AuthenticationRequest;
import sopi.module.auth.model.json.response.AuthenticationResponse;
import sopi.module.auth.model.security.CerberusUser;
import sopi.module.auth.security.TokenUtils;
import sopi.module.person.model.User;
import sopi.module.person.model.UserChangePassword;
import sopi.module.person.model.UserModel;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private final Logger logger = Logger.getLogger(this.getClass());

  private String tokenHeader = "X-Auth-Token";

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenUtils tokenUtils;

  @Autowired
  private UserDetailsService userDetailsService;
  
  @Autowired
  private UserModel userModel;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {

    // Perform the authentication
    Authentication authentication = this.authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        authenticationRequest.getUsername(),
        authenticationRequest.getPassword()
      )
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Reload password post-authentication so we can generate token
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    String token = this.tokenUtils.generateToken(userDetails, device);

    // Return the token
    User user = userModel.getUser(userDetails.getUsername());
    
    if (!user.isEnabled()){
    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(900);
    }
    
    DateTime now = new DateTime(new Date());
    DateTime now2 = now.plusDays(-30);
    if (user.getLastPasswordChangeAt().isBefore(now2)) {
    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(901);
    }
    
    return ResponseEntity.ok(new AuthenticationResponse(token));
  }
  
  @RequestMapping(value="/changePassword", method = RequestMethod.POST)
  public ResponseEntity<?> authenticationRequestChangePassword(@RequestBody UserChangePassword userChangePassword) throws AuthenticationException {

	  try {
		  String result = userModel.changePassword(
					userChangePassword.getUsername(), 
					userChangePassword.getOldPassword(), 
					userChangePassword.getNewPassword(), 
					userChangePassword.getConfirmPassword());
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Wystąpił błąd podczas zmiany hasła: " + e.getMessage());
		}
  }

  @RequestMapping(value = "refresh", method = RequestMethod.GET)
  public ResponseEntity<?> authenticationRequest(HttpServletRequest request) {
    String token = request.getHeader(this.tokenHeader);
    String username = this.tokenUtils.getUsernameFromToken(token);
    CerberusUser user = (CerberusUser) this.userDetailsService.loadUserByUsername(username);
    if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset())) {
      String refreshedToken = this.tokenUtils.refreshToken(token);
      return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }

}
