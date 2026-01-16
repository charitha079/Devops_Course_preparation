package com.example.app;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        dao.createTable();
        dao.addUser(new User(0, "Alice", "alice@example.com"));
        dao.addUser(new User(0, "Bob", "bob@example.com"));

        List<User> users = dao.getAllUsers();
        for(User u: users) {
            System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail());
        }
    }
}
