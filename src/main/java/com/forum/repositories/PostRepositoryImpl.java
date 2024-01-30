package com.forum.repositories;

import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Post;
import com.forum.models.filters.PostFilterOptions;
import com.forum.repositories.contracts.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    public List<Post> get(PostFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Post where (:id is null or id = :id) and " +
                    "(:title is null or title LIKE CONCAT('%', :title, '%')) and " +
                    "(:content is null or content LIKE CONCAT('%', :content, '%')) and " +
                    "(:createdBy is null or createdBy = :createdBy) and isArchived = false"
                    + sortOrder(filterOptions);
            Query<Post> query = session.createQuery(queryStr, Post.class);
            query.setParameter("id", filterOptions.getId().orElse(null));
            query.setParameter("title", filterOptions.getTitle().orElse(null));
            query.setParameter("content", filterOptions.getContent().orElse(null));
            query.setParameter("createdBy", filterOptions.getCreator().orElse(null));
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Not found", "not found", "not found");//todo
            }
            return query.list();
        }
    }

    private String sortOrder(PostFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty())
            return "";
        String orderBy = String.format(" order by %s", filterOptions.getSortBy().get());
        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equals("desc"))
            orderBy = String.format("%s desc", orderBy);
        return orderBy;
    }

    @Override
    public Post get(int id) {
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
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
    public List<Post> getByTitle(String sentence) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title LIKE concat('%',  :sentence, '%')", Post.class);
            query.setParameter("sentence", sentence);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "title", sentence);//todo fix the message
            }
            return result;
        }
    }

    @Override
    public List<Post> getByContent(String sentence) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where content LIKE concat('%',  :sentence, '%')", Post.class);
            query.setParameter("sentence", sentence);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "content", sentence);//todo fix the message
            }
            return result;
        }
    }

    @Override
    public Post create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public Post update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public void archive(int id) {
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
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
