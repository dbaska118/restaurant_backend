package org.example.model.user;

public class LoginRequest {

    private String email;

    private String password;

    private Boolean logout;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password, Boolean logout) {
        this.email = email;
        this.password = password;
        this.logout = logout;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLogout() {
        return logout;
    }

    public void setLogout(Boolean logout) {
        this.logout = logout;
    }
}
