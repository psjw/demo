package com.codesoom.demo.controllers;

import com.codesoom.demo.application.TaskService;
import com.codesoom.demo.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

// TODO
// 1. Read Collecction - GET / tasks => 완료
// 2. Read Item - GET /tasks/{id} => 완료
// 3. Create - POST /tasks => 완료
// 4. Update - PUT/PATCH /tasks/{id} =>
// 5. Delete - DELETE /tasks/{id}
// => 전제 :  Service가 올바를 것
class TaskControllerTest {
    private TaskController controller;
    // 가능한것
    // 1. Real object
    // 2. Mock object
    // 3. Spy ->  Proxy
    //UNIT Test 각각 테스트
    private TaskService taskService;
    @BeforeEach
    void setUp() {
        //taskService = new TaskService();
        //taskService = spy(new TaskService());
        taskService = mock(TaskService.class);

        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTitle("Test");
        tasks.add(task);

        given(taskService.getTask(1L)).willReturn(task);
        given(taskService.getTasks()).willReturn(tasks);
        given(taskService.getTask(100L)).willThrow(new TaskNotFoundException(100L));
        //any() 아무거나 들어와도됨
        //any(Task.class)  Task 클래스만 됨  any 써줄때는 맞춰줘야댐 -> 하나는 eq 넣어줘라 (문법)
        /*
    This exception may occur if matchers are combined with raw values:
        //incorrect:
        someMethod(anyObject(), "raw String");
    When using matchers, all arguments have to be provided by matchers.
    For example:
        //correct:
        someMethod(anyObject(), eq("String by matcher"));
         */
        given(taskService.updateTask(eq(100L), any(Task.class))).willThrow(new TaskNotFoundException(100L));
        given(taskService.deleteTask(100L)).willThrow(new TaskNotFoundException(100L));//return값이 없으면 오류


        controller = new TaskController(taskService);
    }
/*
    @Test
    void listwWithoutTasksSpy() {
        // TODO : service -> returns empty list

        //taskService.getTasks
        // Controller -> Spy -> Real Object
        taskService = spy(new TaskService());
        assertThat(controller.list()).isEmpty();

        verify(taskService).getTasks(); // 실제로 실행하는지 확인가능
        *//*
        Wanted but not invoked:
        taskService.getTasks();
        invoked -> 메서드 실행
        *//*
    }*/

    @Test
    void listwWithoutTasksMock() {
        // TODO : service -> returns empty list
        given(taskService.getTasks()).willReturn(new ArrayList<>());

        //taskService.getTasks
        // Controller -> Spy -> Real Object

        assertThat(controller.list()).isEmpty();

        verify(taskService).getTasks(); // 실제로 실행하는지 확인가능
        /*
        Wanted but not invoked:
        taskService.getTasks();
        invoked -> 메서드 실행
        */
    }
    @Test
    void listWithSomeTasks(){
        // TODO : service -> return list that contains one task.

/*        Task task = new Task();
        task.setTitle("Test");

        controller.create(task);*/
        assertThat(controller.list()).isNotEmpty();

    }


    @Test
    void detailWithExistedID(){
        Task task = controller.detail(1L);
        assertThat(task).isNotNull();
    }
    @Test
    void detailWithNotExistedID(){
        assertThatThrownBy(() -> controller.detail(100L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void createNewTask() {
        Task task = new Task();
        task.setTitle("Test2");

        controller.create(task);
        
        //create에 대한 검증은 service에서
        verify(taskService).createTask(task);
//        task.setTitle("Test2");
//        controller.create(task);
//        assertThat(controller.list()).hasSize(2);
//        assertThat(controller.list()).isNotEmpty();
//        assertThat(controller.list().get(0).getId()).isEqualTo(1L);
//
//        assertThat(controller.list().get(1).getId()).isEqualTo(2L);
//        assertThat(controller.list().get(1).getTitle()).isEqualTo("Test2");

    }

    @Test
    void updateExistedTask()
    {
        Task task = new Task();
        task.setTitle("Renamed Task");

        controller.update(1L, task);

        verify(taskService).updateTask(1L, task);
    }

    @Test
    void updateNotExistedTask()
    {
        Task task = new Task();
        task.setTitle("Renamed Task");
        assertThatThrownBy(() -> controller.update(100L, task)).isInstanceOf(TaskNotFoundException.class);
    }


    @Test
    void deleteExistedTask()
    {

        controller.delete(1L);

        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteNotExistedTask()
    {
        Task task = new Task();
        task.setTitle("Renamed Task");
        assertThatThrownBy(() -> controller.delete(100L)).isInstanceOf(TaskNotFoundException.class);
    }
}