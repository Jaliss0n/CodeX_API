package com.codex.codex_api.models.enums;

public enum UserRole {
    ADMIN("admin"),
    UNITY("unity"),
    USER("user");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
