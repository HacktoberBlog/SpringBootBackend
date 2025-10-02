package com.hacktober.blog.user;

import java.util.List;

public class User {

    private String name;
    private String username; // document id
    private String email;
    private String password; // will be stored as Base64 encrypted
    private List<String> roles;
    private List<String> blogs;

    public User() {
    }

    public User(String name, String username, String email, String password, List<String> roles, List<String> blogs) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.blogs = blogs;
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<String> blogs) {
        this.blogs = blogs;
    }
}
