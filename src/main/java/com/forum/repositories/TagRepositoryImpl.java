package com.forum.repositories;

import com.forum.exceptions.EntityNotFoundException;
import com.forum.models.Tag;
import com.forum.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory sessionFactory;


    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Tag> get() {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery("from Tag", Tag.class);
            return query.list();
        }
    }

    @Override
    public Tag getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery("from Tag where name = :name", Tag.class);
            query.setParameter("name", name);
            List<Tag> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Tag", "name", name);
            }

            return query.list().get(0);
        }
    }

    @Override
    public Tag getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Tag tag = session.get(Tag.class, id);
            if (tag == null) {
                throw new EntityNotFoundException("Tag", id);
            }
            return tag;
        }
    }

    @Override
    public void create(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(tag);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(tag);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Tag tagToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(tagToDelete);
            session.getTransaction().commit();
        }
    }
}
