package com.forum.repositories;

import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Role;
import com.forum.repositories.contracts.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Role get(String role) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role where role = :role", Role.class);
            query.setParameter("role", role);
            List<Role> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Role", "name", role);
            }
            return result.get(0);
        }
    }

    @Override
    public List<Role> get() {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role", Role.class);
            return query.list();
        }
    }
}
