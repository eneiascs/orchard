package com.eneiascs.orchard.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eneiascs.orchard.model.Post;

public interface PostRepo extends JpaRepository<Post, Long> {

	@Query("SELECT p FROM Post p ORDER BY p.postedDate DESC")
	public List<Post> findAll();
	
	@Query("SELECT p FROM Post p WHERE p.username=:username ORDER BY p.postedDate DESC")
	public List<Post> findByUsername(@Param("username") String username);

}
