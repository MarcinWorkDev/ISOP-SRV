package sopi.module.person.model;

import java.sql.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="profile")
public class Profile {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long profileId;
	
	@JsonIgnore
	@OneToOne
	@PrimaryKeyJoinColumn
	User user;
	
	Date dataUrodzenia;
	
	@NotBlank(message="Imię jest wymagane")
	String imie;
	
	String kodPocztowy;
	String miejscowosc;
	
	@NotBlank(message="Nazwisko jest wymagane")
	String nazwisko;
	
	@Email(message="Podano nieprawidłowy adres email")
	String email;
	
	@Pattern(regexp="(^$|0|[0-9]{9})", message="Proszę podać prawidłowy 9-cyfrowy numer telefonu")
	String mobile;
	
	String nrDomu;
	String nrMieszkania;
	String numerDodatkowy;
	String pesel;
	String plec;
	String type;
	String ulica;
	
	String createdBy;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	DateTime createdAt;
	
	public boolean getHasUser(){
		if (this.user != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Profile profile = (Profile) o;
        return Objects.equals( profileId, profile.profileId ) &&
        		Objects.equals( pesel, profile.pesel );
    }

    @Override
    public int hashCode() {
        return Objects.hash( profileId, nazwisko, imie, pesel, email, mobile );
    }
	
	public String getAdres() {
		
		if (nrDomu != null & nrMieszkania != null & nrMieszkania.length() > 0) {
			nrMieszkania = nrDomu + "/" + nrMieszkania;
		} else if (nrDomu != null & nrMieszkania == null) {
			nrMieszkania = nrDomu;
		} else {
			nrMieszkania = "";
		}
		
		String adres = ulica + " " + nrMieszkania + ", " + kodPocztowy + " " + miejscowosc;
		
		if (adres == null) {
			return ""; 
		} else {
			return adres;
		}
	}
	
	public String getPlecName() {
		switch(plec) {
		case "F":
			return "Kobieta";
		case "M":
			return "Mężczyzna";
		default:
			return "Nieznana";
		}
	}
	
	public Long getProfileId() {
		return profileId;
	}
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getDataUrodzenia() {
		return dataUrodzenia;
	}
	public void setDataUrodzenia(Date dataUrodzenia) {
		this.dataUrodzenia = dataUrodzenia;
	}
	public String getImie() {
		return imie;
	}
	public void setImie(String imie) {
		this.imie = imie;
	}
	public String getKodPocztowy() {
		return kodPocztowy;
	}
	public void setKodPocztowy(String kodPocztowy) {
		this.kodPocztowy = kodPocztowy;
	}
	public String getMiejscowosc() {
		return miejscowosc;
	}
	public void setMiejscowosc(String miejscowosc) {
		this.miejscowosc = miejscowosc;
	}
	public String getNazwisko() {
		return nazwisko;
	}
	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}
	public String getNrDomu() {
		return nrDomu;
	}
	public void setNrDomu(String nrDomu) {
		this.nrDomu = nrDomu;
	}
	public String getNrMieszkania() {
		return nrMieszkania;
	}
	public void setNrMieszkania(String nrMieszkania) {
		this.nrMieszkania = nrMieszkania;
	}
	public String getNumerDodatkowy() {
		return numerDodatkowy;
	}
	public void setNumerDodatkowy(String numerDodatkowy) {
		this.numerDodatkowy = numerDodatkowy;
	}
	public String getPesel() {
		return pesel;
	}
	public void setPesel(String pesel) {
		this.pesel = pesel;
	}
	public String getPlec() {
		return plec;
	}
	public void setPlec(String plec) {
		this.plec = plec;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUlica() {
		return ulica;
	}
	public void setUlica(String ulica) {
		this.ulica = ulica;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
