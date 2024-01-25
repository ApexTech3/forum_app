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
    public User get(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }

    @Override
    public List<User> get(UserFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where (:username is null or username = :username) and " +
                    "(:email is null or email = :email) and (:firstName is null or firstName like :firstName)", User.class);
            query.setParameter("username", filterOptions.getUsername().isPresent() ? filterOptions.getUsername().get() : null);
            query.setParameter("email", filterOptions.getEmail().isPresent() ? filterOptions.getEmail().get() : null);
            query.setParameter("firstName", filterOptions.getFirstName().isPresent() ? filterOptions.getFirstName().get() : null);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", "asdf");
            }
            return result;
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

    @Override
    public User update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
            return user;
        }
    }
}
