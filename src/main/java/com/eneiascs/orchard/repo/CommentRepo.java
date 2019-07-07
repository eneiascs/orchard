package com.eneiascs.orchard.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eneiascs.orchard.model.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long>{

}
