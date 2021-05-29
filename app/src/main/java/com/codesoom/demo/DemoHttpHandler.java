package com.codesoom.demo;

import com.codesoom.demo.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO
// 1. Read - collection -> 완료
// 2. Read -> item /element ->  완료
// 3. Create -> 완료
// 4. Update -> 완료
// 5. Delete
public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    public DemoHttpHandler() {
//        Task task = new Task();
//        task.setId(1L);
//        task.setTitle("Do nothing...");
//        tasks.add(task);

//        Task task2 = new Task();
//        task2.setId(2L);
//        task2.setTitle("Second");
//
//        tasks.add(task2);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/" , "/tasks", "task/1" , ...
        // 3/  Headers, Body(Content)

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();


        System.out.println(method + " " + path);

        //early return
        if (path.equals("/tasks")) {
            handleCollection(exchange, method);
            return;
        }
        if (path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.substring("/tasks/".length()));
            System.out.println("id = " + id);
            handleItem(exchange, method, id);
            return;
        }




    }

    private void handleItem(HttpExchange exchange, String method, Long id) throws IOException {
        Task task = findTask(id);
        if(task ==null){
            send(exchange,404,"");
            return;
        }
        if(method.equals("GET")){
            handleDetail(exchange, task);
        }
        if(method.equals("PUT")||method.equals("PATCH")){
            handleUpdate(exchange, task);
        }
        if(method.equals("DELETE")){
            handleDelete(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        tasks.remove(tasks);
        send(exchange,200,"");
    }

    private void handleDetail(HttpExchange exchange,Task task) throws IOException {
        send(exchange,200, toJSON(task));
    }

    private void handleUpdate(HttpExchange exchange, Task task) throws IOException {
        String body = getBody(exchange);
        Task source = toTask(body);
        task.setTitle(source.getTitle());
        send(exchange, 201, toJSON(task));
    }

    private Task findTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst().orElse(null);
    }

    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        if (method.equals("GET")) {
            handleList(exchange);
        }
        if (method.equals("POST")) {
            handleCreate(exchange);
        }
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = getBody(exchange);
        if (!body.isBlank()) {
            Task task = toTask(body);
            task.setId(generateId());
            tasks.add(task);
            send(exchange, 201, toJSON(task));
        }
    }

    private void handleList(HttpExchange exchange) throws IOException {
        send(exchange, 200, tasksToJSON());
    }


    private Long generateId() {
        newId += 1;
        return newId;
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        return toJSON(tasks);
    }

    private String toJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }
}
