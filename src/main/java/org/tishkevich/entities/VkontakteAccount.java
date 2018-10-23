package org.tishkevich.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Vk_User", uniqueConstraints = { @UniqueConstraint(name = "VK_USER_UK", columnNames = "username") })
public class VkontakteAccount {

	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", length = 36, nullable = false)
	private String username;

	public Long getId() {
		return id;
	}

	public void setId(Long userId) {
		this.id = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String encrytedPassword) {
		this.password = encrytedPassword;
	}

	@Column(name = "first_Name", length = 36, nullable = false)
	private String firstName;

	@Column(name = "last_Name", length = 36)
	private String lastName;

	@Column(name = "city", length = 36)
	private String city;

	@Column(name = "photo", length = 128)
	private String photo;

	@Column(name = "password", length = 128, nullable = false)
	private String password;
	
	private boolean active;
	
	private String keycode;
	
	public String getKeycode() {
		return keycode;
	}

	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String id) {
		this.username = id;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
