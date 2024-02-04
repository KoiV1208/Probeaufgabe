package org.example;

import java.util.Date;

public class Person {
    private String name;
    //job id createdAt updatedAt contactdetails
    private String job;
    private String id;
    private String createdAt;
    private String updatedAt;
    private String phonenumber;

    private String email;

    //constructor
    public Person(String id, String name, String job, String createdAt, String updatedAt, String phone, String email) {
        this.name = name;
        this.job = job;
        this.id =id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.phonenumber =phone;
        this.email = email;
    }
    //getter
    public String getName() {
        return this.name;
    }
    public String getJob() {
        return job;
    }
    public String getId() {
        return id;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public String getPhonenumber() {
        return phonenumber;
    }
    public String getEmail() {return email;}

    //setter
    public void setName(String name) {
        this.name = name;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setPhonenumber(String contactdetails) {
        this.phonenumber = contactdetails;
    }
    public void setEmail(String email) {this.email = email;}

}