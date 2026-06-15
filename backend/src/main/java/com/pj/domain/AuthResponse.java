package com.pj.domain;

public class AuthResponse {
    private boolean success;
    private String message;
    private String username;

    public static AuthResponse ok(String username) {
        AuthResponse r = new AuthResponse();
        r.success = true;
        r.message = "操作成功";
        r.username = username;
        return r;
    }

    public static AuthResponse fail(String message) {
        AuthResponse r = new AuthResponse();
        r.success = false;
        r.message = message;
        return r;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
