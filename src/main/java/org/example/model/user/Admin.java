package org.example.model.user;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    public Admin() {
        this.setRole("admin");
    }

    public Admin(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
        this.setRole("admin");
    }
}
