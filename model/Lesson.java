package com.example.l7.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Lesson {
    @Id
    private Integer id;
    private String title;
    private String time;
    private String teacher;

    public Lesson() {
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    public Lesson(int id, String title, String time, String teacher){
        this.id = id;
        this.title = title;
        this.time = time;
        this.teacher = teacher;
    }
}
