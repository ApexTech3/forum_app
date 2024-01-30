package com.forum.services;

import com.forum.exceptions.AuthorizationException;
import com.forum.exceptions.EntityDuplicateException;
import com.forum.exceptions.EntityNotFoundException;
import com.forum.helpers.AuthenticationHelper;
import com.forum.models.Tag;
import com.forum.models.User;
import com.forum.repositories.contracts.TagRepository;
import com.forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private static final String MODIFY_TAG_ERROR_MESSAGE = "Only admin or the tag creator can modify the tag.";

    private final TagRepository repository;


    @Autowired
    public TagServiceImpl(TagRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Tag> get() {
        return repository.get();
    }

    @Override
    public Tag getByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public Tag getById(int id) {
        return repository.getById(id);
    }

    @Override
    public Tag create(Tag tag) {
        boolean duplicateExists = true;
        try {
            repository.getByName(tag.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Tag", "name", tag.getName());
        }

        return repository.create(tag);
    }


    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(user);
        repository.delete(id);
    }


    private void checkModifyPermissions(User user) {
        if (!(AuthenticationHelper.isAdmin(user))) {
        throw new AuthorizationException(MODIFY_TAG_ERROR_MESSAGE);
        }
    }

}