package com.codesoom.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//1. Entity(Domain)
//2. RDB의 Entity와 다름 주의!
// -> JPA의 Entity 역할도 같이 하기로함.
@Entity
public class Task {
    // Identifier - Unique Key
    @Id
    @GeneratedValue
    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
