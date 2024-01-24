package com.forum.repositories;

import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Comment;
import com.forum.repositories.contracts.CommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comment> get() {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment", Comment.class);
            return query.list();
        }
    }

    @Override
    public List<Comment> getByPostId(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment where parentPost = :postId", Comment.class);
            query.setParameter("postId", postId);
            return query.list();
        }
    }
    @Override
    public List<Comment> getByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment where createdBy = :userId", Comment.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public Comment get(int id) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, id);
            if (comment == null) {
                throw new EntityNotFoundException("Comment", id);
            }
            return comment;
        }
    }

    @Override
    public void create(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Comment commentToDelete = get(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }
    }




}
