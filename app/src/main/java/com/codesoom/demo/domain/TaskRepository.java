package com.codesoom.demo.domain;

import com.codesoom.demo.controllers.TaskNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(Long id);

    public Task save(Task task);

    void delete(Task task);

}
