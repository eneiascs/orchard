package com.eneiascs.orchard.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eneiascs.orchard.model.Comment;
import com.eneiascs.orchard.repo.CommentRepo;
import com.eneiascs.orchard.service.CommentService;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepo commentRepo;
	@Override
	public void saveComment(Comment comment) {
		commentRepo.save(comment);
	}

}
