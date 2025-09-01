    package com.discryptment.model;

public class Config {
    private Integer id;
    private String authPasswordHash; // store bcrypt hash here
    private Long adminId;

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getAuthPasswordHash() { return authPasswordHash; }
    public void setAuthPasswordHash(String authPasswordHash) { this.authPasswordHash = authPasswordHash; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
}
