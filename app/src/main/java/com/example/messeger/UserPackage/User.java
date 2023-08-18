package com.example.messeger.UserPackage;

public class User {

    private String id;
    private String name;
    private String lastname;
    private int age;
    private boolean isOnline;

    public User(String id, String name, String lastname, int age, boolean isOnline) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.age = age;
        this.isOnline = isOnline;
    }

    public User(String name, String lastname, int age, boolean status) {
        this.name = name;
        this.lastname = lastname;
        this.age = age;
        this.isOnline = status;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public boolean isOnline() {
        return isOnline;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", isOnline=" + isOnline +
                '}';
    }
}
