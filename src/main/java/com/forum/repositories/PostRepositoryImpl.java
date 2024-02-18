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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

    public Page<Post> findAll(int page, int size, PostFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<Post> resultList = session.createQuery("FROM Post WHERE isArchived = false", Post.class)

                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .list();

            long totalCount = session.createQuery("SELECT COUNT(*) FROM Post", Long.class)

                    .uniqueResult();

            return new PageImpl<>(resultList, PageRequest.of(page - 1, size), totalCount);
        }
    }

    @Override
    public List<Post> get(PostFilterOptions filterOptions) {

        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Post p " +
                    "where (:id is null or p.id = :id) and " +
                    "(:title is null or p.title LIKE CONCAT('%', :title, '%')) and " +
                    "(:content is null or p.content LIKE CONCAT('%', :content, '%')) and " +
                    "(:createdBy is null or p.createdBy.id = :createdBy) and " +
                    "(EXISTS (SELECT 1 FROM p.tags t WHERE t.tagId IN (:tagIds))) AND " +
                    " p.isArchived = false " +
                    sortOrder(filterOptions);

            Query<Post> query = session.createQuery(queryStr, Post.class);
            query.setParameter("id", filterOptions.getId().orElse(null));
            query.setParameter("title", filterOptions.getTitle().orElse(null));
            query.setParameter("content", filterOptions.getContent().orElse(null));
            query.setParameter("createdBy", filterOptions.getCreator().orElse(null));
            query.setParameterList("tagIds", filterOptions.getTags().orElse(null));
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("No posts were found within the criteria.");
            }
            return query.list();
        }
    }

    @Override
    public List<Post> getByContentOrTitle(PostFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Post p " +
                    "where (:title is null or p.title LIKE CONCAT('%', :title, '%')) or " +
                    "(:content is null or p.content LIKE CONCAT('%', :content, '%')) or " +
                    "(:content is null or p.createdBy.username LIKE CONCAT('%',:content,'%')) "
                    + sortOrder(filterOptions);
            Query<Post> query = session.createQuery(queryStr, Post.class);
            query.setParameter("title", filterOptions.getTitle().orElse(null));
            query.setParameter("content", filterOptions.getContent().orElse(null));
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("No posts were found within the criteria.");
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
                throw new EntityNotFoundException("Post", "id", String.valueOf(id));
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
                throw new EntityNotFoundException("Post", "title", title);
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
                throw new EntityNotFoundException("Post", "creator ID", String.valueOf(userId));
            }
            return result;
        }
    }

    @Override
    public List<Post> getBySimilarTitle(String sentence) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title LIKE concat('%',  :sentence, '%')", Post.class);
            query.setParameter("sentence", sentence);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "title which contains", sentence);
            }
            return result;
        }
    }

    @Override
    public Post getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title", title);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "title", title);
            }
            return result.get(0);
        }
    }

    @Override
    public List<Post> getByContent(String sentence) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where content LIKE concat('%',  :sentence, '%')", Post.class);
            query.setParameter("sentence", sentence);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "content which contains", sentence);
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
            String sql = "INSERT INTO forum.likes_dislikes (post_id, user_id, like_dislike) VALUES " +
                    "(:postId, :userId, :likeDislike) on duplicate key update like_dislike = :likeDislike";
            Query<?> nativeQuery = session.createNativeQuery(sql, Integer.class);
            nativeQuery.setParameter("postId", postId);
            nativeQuery.setParameter("userId", userId);
            nativeQuery.setParameter("likeDislike", likeDislike);
            nativeQuery.executeUpdate();
            transaction.commit();
            return get(postId);
        }
    }

    public List<Post> getPostsByTag(String tagName) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            // Create HQL query to fetch posts by tag name
            String hql = "SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tagName";
            Query<Post> query = session.createQuery(hql, Post.class);
            query.setParameter("tagName", tagName);
            List<Post> posts = query.getResultList();
            transaction.commit();
            return posts;
        }
    }




}
