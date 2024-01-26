package com.forum.repositories;

import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.PostMapper;
import com.forum.models.Post;
import com.forum.models.dtos.PostResponseDto;
import com.forum.repositories.contracts.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {


    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where isArchived = false", Post.class);
            return query.list();
        }
    }

    @Override
    public Post get(int id) {
        try (Session session = sessionFactory.openSession()){
            Query<Post> query = session.createQuery("from Post  where id = :id", Post.class);
            query.setParameter("id", id);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("id", id);
            }
            return result.get(0);
        }
    }

    @Override
    public Post get(String title) {
        try (Session session = sessionFactory.openSession()){
            Query<Post> query = session.createQuery("from Post  where title = :title", Post.class);
            query.setParameter("title", title);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("title", "title", title);//todo
            }
            return result.get(0);
        }
    }

    @Override
    public List<Post> getByUserId(int userId) {
        try (Session session = sessionFactory.openSession()){
            Query<Post> query = session.createQuery("from Post where createdBy.id = :userId", Post.class);
            query.setParameter("userId", userId);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", userId);//todo fix the message
            }
            return result;
        }
    }

    @Override
    public Post create(Post post) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public Post update(Post post) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public void archive(int id) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            Query<Post> query = session.createQuery("update Post set isArchived = true" +
                    " where id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        }
    }
}
