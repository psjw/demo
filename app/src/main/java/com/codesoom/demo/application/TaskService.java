package com.codesoom.demo.application;

import com.codesoom.demo.controllers.TaskNotFoundException;
import com.codesoom.demo.domain.Task;
import com.codesoom.demo.domain.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {
    // 1. list -> getTasks
    // 2. detail -> getTask (with ID)
    // 3. create -> createTask (with source)
    // 4. update -> updateTask (with ID, source)
    // 5. delete -> deleteTask (with ID)
    private  final TaskRepository taskRepository;
    private Long newId = 0L;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
//        this.taskRepository = new TaskRepository();
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));
    }

    public Task createTask(Task source) {
        Task task = new Task();
        if (source.getTitle().isBlank()) {
            // TODO : validation error....
        }
        task.setTitle(source.getTitle());

        return taskRepository.save(task);
    }



    public Task updateTask(Long id, Task source) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));
        task.setTitle(source.getTitle());
        return task;
    }

    public Task deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
        return task;
    }

}
