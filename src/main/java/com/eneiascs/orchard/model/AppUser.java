package com.eneiascs.orchard.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class AppUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5342767168711197762L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	private Long id;
	private String name;
	
	@Column(unique = true)
	private String username;
	private String password;
	private String email;
	
	@Column(columnDefinition = "text")
	private String bio;
	private Date createdDate;
	
	@OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<UserRole> userRoles;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Post> posts = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Post> likedPosts = new ArrayList<>();
	
	
	
	public AppUser() {
		
	}


	public AppUser(String name, String username, String email) {
		super();
		this.name = name;
		this.username = username;
		this.email = email;
	}


	public AppUser(Long id, String name, String username, String password, String email, String bio,
			Date createdDate, Set<UserRole> userRoles, List<Post> posts, List<Post> likedPosts) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.bio = bio;
		this.createdDate = createdDate;
		this.userRoles = userRoles;
		this.posts = posts;
		this.likedPosts = likedPosts;
	}
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
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



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getBio() {
		return bio;
	}



	public void setBio(String bio) {
		this.bio = bio;
	}



	public Date getCreatedDate() {
		return createdDate;
	}



	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}



	public Set<UserRole> getUserRoles() {
		if(userRoles == null) {
			userRoles = new HashSet<>();
		}
		return userRoles;
	}



	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}



	public List<Post> getPosts() {
		if(posts == null) {
			posts = new ArrayList<>();
		}
		return posts;
	}

	

	public List<Post> getLikedPosts() {
		return likedPosts;
	}



	public void setLikedPosts(List<Post> likedPosts) {
		this.likedPosts = likedPosts;
	}


	public void addPost(Post post) {
		getPosts().add(post);
		
	}


	public void addLikedPost(Post post) {
		likedPosts.add(post);
	}

	public void removeLikedPost(Post post) {
		likedPosts.remove(post);
	}


	public void addUserRole(UserRole userRole) {
		getUserRoles().add(userRole);
		
	}
	



	
	
}
