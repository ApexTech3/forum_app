package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.models.Comment;
import com.forum.models.User;
import com.forum.repositories.contracts.CommentRepository;
import com.forum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static final String MODIFY_COMMENT_ERROR_MESSAGE = "Only admin or comment creator can modify the comment.";

    private final CommentRepository repository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Comment> get() {
        return repository.get();
    }

    @Override
    public List<Comment> getByPostId(int postId) {
        return repository.getByPostId(postId);
    }

    @Override
    public List<Comment> getByUserId(int userId) {
        return repository.getByUserId(userId);
    }

    @Override
    public Comment get(int id) {
        return repository.get(id);
    }

    @Override
    public Comment create(Comment comment) {return repository.create(comment);}

    @Override
    public Comment update(Comment comment, User user) {
        checkModifyPermissions(comment.getCommentId(), user);

        return repository.update(comment);

    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);

        repository.delete(id);
    }

    private void checkModifyPermissions(int id, User user) {
        Comment comment = repository.get(id);
        if (!(AuthenticationHelper.isAdmin(user) || comment.getCreatedBy().equals(user))){
            throw new AuthorizationException(MODIFY_COMMENT_ERROR_MESSAGE);
        }
    }

}
