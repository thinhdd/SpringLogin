
package com.login.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "user")
@Table(name = "user")
public class UserBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String email;
	
	private String firstName;
	
	private String lastName;

	private String title;
	private String country;
	private String password;
	
	private String accesstoken;
	
	public String getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	@Transient
	private String passwordConfirm;	
	private String provider;
	private String image;
	
	
	public String getEmail() {
		return email;
	}

	public String getImage() {
	    return image;
	}

	public void setImage(String image) {
	    this.image = image;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	 

}
