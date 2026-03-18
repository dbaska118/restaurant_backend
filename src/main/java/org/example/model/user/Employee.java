package org.example.model.user;


import jakarta.persistence.Entity;

@Entity
public class Employee extends User{

    public Employee() {
        this.setRole("employee");
    }

    public Employee(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
        this.setRole("employee");
    }
}
