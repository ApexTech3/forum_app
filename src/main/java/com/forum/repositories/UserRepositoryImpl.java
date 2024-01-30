package com.forum.repositories;

import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.User;
import com.forum.models.filters.UserFilterOptions;
import com.forum.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User get(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User get(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username and isDeleted = false", User.class);
            query.setParameter("username", username);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email and isDeleted = false", User.class);
            query.setParameter("email", email);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }


    @Override
    public List<User> get(UserFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from User where (:username is null or username = :username) and " +
                    "(:email is null or email = :email) and (:firstName is null or firstName like :firstName) and isDeleted = false "
                    + sortOrder(filterOptions);
            Query<User> query = session.createQuery(queryStr, User.class);
            query.setParameter("username", filterOptions.getUsername().orElse(null));
            query.setParameter("email", filterOptions.getEmail().orElse(null));
            query.setParameter("firstName", filterOptions.getFirstName().orElse(null));
            return query.list();
        }
    }

    @Override
    public User register(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            return user;
        }
    }

    private String sortOrder(UserFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty())
            return "";
        String orderBy = String.format(" order by %s", filterOptions.getSortBy().get());
        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equals("desc"))
            orderBy = String.format("%s desc", orderBy);
        return orderBy;
    }

    @Override
    public User update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public User delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = get(id);
            user.setDeleted(true);
            session.merge(user);
            session.getTransaction().commit();
            return user;
        }
    }
}
