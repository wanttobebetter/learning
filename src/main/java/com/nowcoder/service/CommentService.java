package com.nowcoder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;

@Service
public class CommentService {
@Autowired
private CommentDAO commentDAO;


public List<Comment> getCommentsByEntity(int entityId,int entityType){
	return commentDAO.selectByEntity(entityId, entityType);
}

public int addComment(Comment comment) {
	return commentDAO.addComment(comment);
}

public int getCommentCount(int entityId,int entityType) {
	return commentDAO.getCommentCount(entityId, entityType);
}

public void deleteComment(int entityId,int entityType) {
	commentDAO.updateStatus(entityId, entityType, 1);
}
}
