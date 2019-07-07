package com.eneiascs.orchard.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eneiascs.orchard.model.AppUser;
import com.eneiascs.orchard.model.Comment;
import com.eneiascs.orchard.model.Post;
import com.eneiascs.orchard.service.AccountService;
import com.eneiascs.orchard.service.CommentService;
import com.eneiascs.orchard.service.PostService;

@RestController
@RequestMapping("/post")
public class PostResource {
	private String postImageName;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/list")
	public List<Post> getPostList(){
		return postService.postList();		
	}
	
	@GetMapping("/getPostById/{postId}")
	public Post getPostbyId(@PathVariable("postId") Long id) {
		return postService.getPostById(id).get();
	}
	
	@GetMapping("/getPostByUsername/{username}")
	public ResponseEntity<?> getPostByUsername(@PathVariable("username") String username){
		AppUser user = accountService.findByUsername(username);
		if(user == null) {
			return new ResponseEntity<>(ResponseEnum.USER_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		try {
			List<Post> posts = postService.findPostByUsername(username);
			return new ResponseEntity<>(posts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ResponseEnum.ERROR, HttpStatus.BAD_REQUEST);

		}
		
	}
	
	
	@PostMapping("/save")
	public ResponseEntity<?> savePost(@RequestBody HashMap<String, String> request) {
		String username = request.get("username");
		AppUser user = accountService.findByUsername(username);
		if(user == null) {
			return new ResponseEntity<>(ResponseEnum.USER_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		postImageName = RandomStringUtils.randomAlphabetic(10);
		try {
			Post post = postService.savePost(user, request, postImageName);
			return new ResponseEntity<>(post,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(ResponseEnum.ERROR, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id){
		Optional<Post> post = postService.getPostById(id);
		if(post.isPresent()) {
			try {
				postService.deletePost(post.get());
				return new ResponseEntity<>(post, HttpStatus.OK);

			} catch (Exception e) {
				return new ResponseEntity<>(ResponseEnum.ERROR, HttpStatus.BAD_REQUEST);
			}
		}else {
			return new ResponseEntity<>(ResponseEnum.POST_NOT_FOUND,HttpStatus.NOT_FOUND);

		}
		
	}
	
	
	@PostMapping("/like")
	public ResponseEntity<ResponseEnum> likePost(@RequestBody HashMap<String, String> request){
		String username = request.get("username");
		AppUser user = accountService.findByUsername(username);
		if(user == null) {
			return new ResponseEntity<>(ResponseEnum.USER_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		
		String id = request.get("postId");
		Optional<Post> post = postService.getPostById(Long.parseLong(id));
		if(post.isPresent()) {
			try {
				post.get().addLike();
				user.addLikedPost(post.get());
				return new ResponseEntity<>(ResponseEnum.POST_LIKED, HttpStatus.OK);

			} catch (Exception e) {
				return new ResponseEntity<>(ResponseEnum.ERROR, HttpStatus.BAD_REQUEST);
			}
		}else {
			return new ResponseEntity<>(ResponseEnum.POST_NOT_FOUND,HttpStatus.NOT_FOUND);

		}
	}
	
	@PostMapping("/unlike")
	public ResponseEntity<ResponseEnum> unlikePost(@RequestBody HashMap<String, String> request){
		String username = request.get("username");
		AppUser user = accountService.findByUsername(username);
		if(user == null) {
			return new ResponseEntity<>(ResponseEnum.USER_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		
		String id = request.get("postId");
		Optional<Post> post = postService.getPostById(Long.parseLong(id));
		if(post.isPresent()) {
			try {
				post.get().removeLike();
				user.removeLikedPost(post.get());
				return new ResponseEntity<>(ResponseEnum.POST_UNLIKED, HttpStatus.OK);

			} catch (Exception e) {
				return new ResponseEntity<>(ResponseEnum.ERROR, HttpStatus.BAD_REQUEST);
			}
		}else {
			return new ResponseEntity<>(ResponseEnum.POST_NOT_FOUND,HttpStatus.NOT_FOUND);

		}
	}
	
	@PostMapping("/comment/add")
	public ResponseEntity<?> addComment(@RequestBody HashMap<String, String> request){
		String username = request.get("username");
		AppUser user = accountService.findByUsername(username);
		if(user == null) {
			return new ResponseEntity<>(ResponseEnum.USER_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
		
		String id = request.get("postId");
		Optional<Post> post = postService.getPostById(Long.parseLong(id));
		if(post.isPresent()) {
			String content = request.get("content");
			try {
				Comment comment = new Comment();
				comment.setContent(content);
				comment.setPostedDate(new Date());
				comment.setUsername(username);
				post.get().addComment(comment);
				commentService.saveComment(comment);
				
				return new ResponseEntity<>(comment, HttpStatus.OK);

			} catch (Exception e) {
				return new ResponseEntity<>(ResponseEnum.COMMENT_NOT_ADDED, HttpStatus.BAD_REQUEST);
			}
		}else {
			return new ResponseEntity<>(ResponseEnum.POST_NOT_FOUND,HttpStatus.NOT_FOUND);

		}
	}
	
	@PostMapping("/photo/upload")
	public ResponseEntity<ResponseEnum> fileUpload(HttpServletRequest request){
		try {
			postService.savePostImage(request, postImageName);
			return new ResponseEntity<>(ResponseEnum.USER_PICTURE_SAVED, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ResponseEnum.USER_PICTURE_NOT_SAVED, HttpStatus.BAD_REQUEST);
		}

	}

}
