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
    private final PostMapper postMapper;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory, PostMapper postMapper) {
        this.sessionFactory = sessionFactory;
        this.postMapper = postMapper;
    }

    @Override
    public List<PostResponseDto> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where isArchived = false", Post.class);
            return postMapper.fromPostListToResponseDto(query.list());
        }
    }

    @Override
    public PostResponseDto get(int id) {
        try (Session session = sessionFactory.openSession()){
            Query<Post> query = session.createQuery("from Post  where id = :id", Post.class);
            query.setParameter("id", id);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("id", id);
            }
            List<Post> resultList = new ArrayList<>();
            resultList.add(result.get(0));
            return postMapper.fromPostListToResponseDto(resultList).get(0);
        }
    }

    @Override
    public List<PostResponseDto> getByUserId(int userId) {
        try (Session session = sessionFactory.openSession()){
            Query<Post> query = session.createQuery("from Post where createdBy.id = :userId", Post.class);
            query.setParameter("userId", userId);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", userId);//todo fix the message
            }
            return postMapper.fromPostListToResponseDto(result);
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
