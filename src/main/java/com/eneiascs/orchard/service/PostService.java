package com.eneiascs.orchard.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.eneiascs.orchard.model.AppUser;
import com.eneiascs.orchard.model.Post;

public interface PostService {
	public Post savePost(AppUser appUser, Map<String, String> request, String postImageName);
	
	public List<Post> postList();
	
	public Optional<Post> getPostById(Long id);
	
	public List<Post> findPostByUsername(String username);
	
	public Post deletePost(Post post);
	
	public String savePostImage(HttpServletRequest request, String fileName);
}
