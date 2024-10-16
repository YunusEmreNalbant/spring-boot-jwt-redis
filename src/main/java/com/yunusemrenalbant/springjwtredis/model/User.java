package com.yunusemrenalbant.springjwtredis.model;


import com.yunusemrenalbant.springjwtredis.enums.Role;
import com.yunusemrenalbant.springjwtredis.enums.TokenClaims;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Map<String, Object> getClaims() {
        final Map<String, Object> claims = new HashMap<>();

        claims.put(TokenClaims.ID.getValue(), this.id);
        claims.put(TokenClaims.ROLES.getValue(), List.of(this.role));
        claims.put(TokenClaims.FIRST_NAME.getValue(), this.firstName);
        claims.put(TokenClaims.LAST_NAME.getValue(), this.lastName);
        claims.put(TokenClaims.EMAIL.getValue(), this.email);

        return claims;
    }

    public User() {
    }

    public User(Long id, String firstName, String lastName, String username, String email, String password, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
