package app.feedback.auth;

import app.feedback.auth.dto.Authentication;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AutoLogin {

    @Id
    private String id;
    @Embedded
    private Authentication authentication;


    protected AutoLogin() {
    }

    public AutoLogin(final String id, final Authentication authentication) {
        this.id = id;
        this.authentication = authentication;
    }

    public String getId() {
        return id;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}