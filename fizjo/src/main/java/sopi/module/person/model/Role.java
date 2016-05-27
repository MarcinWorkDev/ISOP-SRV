package sopi.module.person.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="role")
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2635593534375477787L;

	@Id
	String role;
	
	String description;
	
	public Role() {
		
	}
	
	public Role(String role, String description) {
		this.role = role;
		this.description = description;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRole() {
		return role;
	}
	
	@Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Role address = (Role) o;
        return Objects.equals( role, address.role ) &&
                Objects.equals( description, address.description );
    }

    @Override
    public int hashCode() {
        return Objects.hash( role, description );
    }
	
}
