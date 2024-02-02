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
                    "(:createdBy is null or createdBy.id = :createdBy) and isArchived = false"
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

    @Override
    public long getCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("select count(*)from Post where isArchived = false", Long.class);
            return query.uniqueResult();
        }
    }

    @Override
    public List<Post> getMostCommented() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "SELECT p " +
                            "FROM Comment c " +
                            "LEFT JOIN c.parentPost p " +
                            "ON c.parentPost.id = p.id " +
                            "GROUP BY p.id " +
                            "ORDER BY COUNT(c.parentPost) DESC ", Post.class).setMaxResults(10);
            return query.list();
        }
    }

    @Override
    public List<Post> getMostLiked() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                            "SELECT p " +
                                    "FROM Post p " +
                                    "ORDER BY p.likes DESC", Post.class)
                    .setMaxResults(10);
            return query.list();
        }
    }

    @Override
    public List<Post> getRecentlyCreated() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                            "SELECT p " +
                                    "FROM Post p " +
                                    "ORDER BY p.stampCreated DESC", Post.class)
                    .setMaxResults(10);
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
                    " where id = :id", Post.class);
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public Post likeDislike(int userId, int postId, String likeDislike) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "INSERT INTO  likes_dislikes (post_id, user_id, like_dislike) VALUES " +
                    "(:postId, :userId, :likeDislike) on duplicate key update like_dislike = :likeDislike";
            Query<?> nativeQuery = session.createNativeQuery(sql, Integer.class);
            nativeQuery.setParameter("postId", postId);
            nativeQuery.setParameter("userId", userId);
            nativeQuery.setParameter("likeDislike", likeDislike);
            nativeQuery.setParameter("likeDislike", likeDislike);
            nativeQuery.executeUpdate();
            transaction.commit();
            return get(postId);
        }
    }


}
