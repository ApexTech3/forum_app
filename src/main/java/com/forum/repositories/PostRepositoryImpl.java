package com.forum.repositories;

import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Post;
import com.forum.repositories.contracts.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;

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

    @Override
    public void like(int user_id, int post_id) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            String sql = "INSERT INTO likes_dislikes (post_id, user_id, like_dislike)\n" +
                    " VALUES (:post_id, :user_id, 'LIKE');";
            Query nativeQuery = session.createNativeQuery(sql);
            nativeQuery.setParameter("post_id", post_id);
            nativeQuery.setParameter("user_id", user_id);
            nativeQuery.executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public void dislike(int user_id, int post_id) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            String sql = "INSERT INTO likes_dislikes (post_id, user_id, like_dislike)\n" +
                    " VALUES (:post_id, :user_id, 'DISLIKE');";
            Query nativeQuery = session.createNativeQuery(sql);
            nativeQuery.setParameter("post_id", post_id);
            nativeQuery.setParameter("user_id", user_id);
            nativeQuery.executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public boolean userLikedPost(int user_id, int post_id) {
        try (Session session = sessionFactory.openSession()){
            String sql = "SELECT like_dislike from likes_dislikes WHERE post_id = :post_id and user_id = :user_id and like_dislike = :str";
            Query nativeQuery = session.createNativeQuery(sql);
            nativeQuery.setParameter("user_id", user_id);
            nativeQuery.setParameter("post_id", post_id);
            nativeQuery.setParameter("str", "LIKE");
            List<String> result = nativeQuery.getResultList();
            if (result.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public boolean userDislikedPost(int user_id, int post_id) {
        try (Session session = sessionFactory.openSession()){
            String sql = "SELECT like_dislike from likes_dislikes WHERE post_id = :post_id and user_id = :user_id and like_dislike = :str";
            Query nativeQuery = session.createNativeQuery(sql);
            nativeQuery.setParameter("user_id", user_id);
            nativeQuery.setParameter("post_id", post_id);
            nativeQuery.setParameter("str", "DISLIKE");
            List<String> result = nativeQuery.getResultList();
            if (result.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }
    }


}
