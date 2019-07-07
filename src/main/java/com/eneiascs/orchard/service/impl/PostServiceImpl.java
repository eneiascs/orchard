package com.eneiascs.orchard.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eneiascs.orchard.model.AppUser;
import com.eneiascs.orchard.model.Post;
import com.eneiascs.orchard.repo.PostRepo;
import com.eneiascs.orchard.service.PostService;
import com.eneiascs.orchard.utility.Constants;

@Transactional
@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepo postRepo;
	
	@Override
	public Post savePost(AppUser appUser, Map<String, String> request, String postImageName) {
		String caption = request.get("caption");
		String location = request.get("location");
		Post post = new Post();
		post.setCaption(caption);
		post.setLocation(location);
		post.setUsername(appUser.getUsername());
		post.setPostedDate(new Date());
		post.setUserImageId(appUser.getId());
		appUser.addPost(post);
		postRepo.save(post);
		return post;
	}

	@Override
	public List<Post> postList() {
		return postRepo.findAll();
	}

	@Override
	public Optional<Post> getPostById(Long id) {
		return postRepo.findById(id);
	}

	@Override
	public List<Post> findPostByUsername(String username) {
		return postRepo.findByUsername(username);
	}

	@Override
	public Post deletePost(Post post) {
		try {
			Files.deleteIfExists(Paths.get(Constants.POST_FOLDER + "/" + post.getName() + ".png"));
			postRepo.deleteById(post.getId());
			return post;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String savePostImage(HttpServletRequest request, String fileName) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> it = multipartRequest.getFileNames();
		MultipartFile multipartFile = multipartRequest.getFile(it.next());
		try {
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(Constants.POST_FOLDER + fileName + ".png");
			Files.write(path, bytes, StandardOpenOption.CREATE);
			return "Photo saved successfully";
		} catch (IOException e) {
			
			e.printStackTrace();
			return "Error occured. Photo not saved.";
		}
		
	
	}

}
