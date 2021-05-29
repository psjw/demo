package com.codesoom.demo.controllers;

import com.codesoom.demo.application.TaskService;
import com.codesoom.demo.domain.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO
// 1. Read Collecction - GET / tasks => 완료
// 2. Read Item - GET /tasks/{id} =>완료
// 3. Create - POST /tasks => 완료
// 4. Update - PUT/PATCH /tasks/{id} => 완료
// 5. Delete - DELETE /tasks/{id}
@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {
    private final TaskService taskService;

    public  TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //    @RequestMapping(path="", method= RequestMethod.GET)
    @GetMapping
    public List<Task> list() {
        return taskService.getTasks();
    }

    @GetMapping("{id}")
    public Task detail(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    //    @RequestMapping(path="", method=RequestMethod.POST)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PatchMapping("{id}")
    public Task patch(@PathVariable Long id, @RequestBody Task source) {
        return taskService.updateTask(id, source);
    }

    @PutMapping("{id}")
    public Task update(@PathVariable Long id, @RequestBody Task source) {
        return taskService.updateTask(id, source);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
