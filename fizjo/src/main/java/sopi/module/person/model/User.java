package sopi.module.person.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2618864590250620291L;

	@Id
	@Column(name="userId")
	Long userId;
	
	@NotBlank(message="Nazwa użytkownika nie może być pusta")
	@Size(min=5, max=25, message="Nazwa użytkownika musi mieć przynajmniej {min} znaków i nie więcej niż {max} znaków")
	@Column(unique = true)
	String username;
	
	@JsonIgnore
	String password;
	
	@NotNull
	boolean enabled;
	
	@NotNull
	boolean credentialsNonExpired;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	DateTime lastPasswordChangeAt;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	DateTime createdAt;
	
	String createdBy;
	
	@OneToOne(mappedBy = "user")
	Profile profile;
	
	//@ElementCollection
	//@CollectionTable(name="user_role", joinColumns=@JoinColumn(name="users_userId"))
	@JsonIgnore
    @OneToMany
    @JoinTable(name="user_role")
    @JoinColumn(name="user_userId")
	List<Role> roles = new ArrayList<>();
	
	public User() {
		
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.enabled = true;
		this.credentialsNonExpired = true;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public DateTime getLastPasswordChangeAt() {
		return lastPasswordChangeAt;
	}

	public void setLastPasswordChangeAt(DateTime lastPasswordChangeAt) {
		this.lastPasswordChangeAt = lastPasswordChangeAt;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
}
