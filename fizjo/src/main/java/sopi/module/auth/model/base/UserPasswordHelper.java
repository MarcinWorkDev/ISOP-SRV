package sopi.module.auth.model.base;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPasswordHelper {
	
	private PasswordEncoder passwordEncoder;
	
	// Nowe podane haslo
	private String newPassword;
	
	// Haslo stare podane
	private String oldPassword;
	
	// Nowe potwierdzajace haslo
	private String confirmPassword;
	
	// Haslo aktualne w bazie
	private String password;
	private String encodedPassword;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String error;
	
	/// CONSTRUCTORS
	
	public UserPasswordHelper(PasswordEncoder passwordEncoder, String password, String oldPassword, String newPassword, String confirmPassword) {
		this.password = password;
		this.newPassword = newPassword;
		this.oldPassword = oldPassword;
		this.confirmPassword = confirmPassword;
		this.passwordEncoder = passwordEncoder;
	}
	
	public UserPasswordHelper(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	/// METHODS
	
	private boolean checkPassword() {
		
		if (!this.passwordEncoder.matches(this.getOldPassword(), this.getPassword())) {
			this.setError("Podane aktualne hasło nie jest poprawne.");
			return false;
		}
		
		if (!(this.getNewPassword()).equals(this.getConfirmPassword())) {
			this.setError("Podane hasła różnią się od siebie.");
			return false;
		}
		
		if ((this.getNewPassword()).equals(this.getOldPassword())) {
			this.setError("Nowe hasło musi być inne niż poprzednie.");
			return false;
		}
		
		return true;
	}
	
	public void newPassword() {
		this.setRandomPassword();
	}
	
	public boolean changePassword() {
		if (this.checkPassword()) {
			this.setEncodedPassword(this.encodePassword(this.getNewPassword()));
			return true;
		} else {
			return false;
		}
	}
		
	private String encodePassword(String pass) {
		String encode = passwordEncoder.encode(pass).toString();
		return encode;
	}
	
	private void setRandomPassword() {
		String randomPassword = RandomStringUtils.randomAlphanumeric(10);
		this.setPassword(randomPassword);
		this.encodedPassword = this.encodePassword(randomPassword);
	}
	
	/// SETS GETS

	private String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	private String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	private String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getError() {
		return error;
	}

	private void setError(String error) {
		this.error = error;
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	private void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

}