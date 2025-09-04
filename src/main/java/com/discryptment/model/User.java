package com.discryptment.model;

public class User {
    private int id;
    private long telegramId;
    private String username;
    private double goldBalance;
    private double realUsdBalance;
    private double expectedUsdTotal;
    private boolean authorized;
    private boolean started;

    // --- Getters and setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public long getTelegramId() { return telegramId; }
    public void setTelegramId(long telegramId) { this.telegramId = telegramId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public double getGoldBalance() { return goldBalance; }
    public void setGoldBalance(double goldBalance) { this.goldBalance = goldBalance; }

    public double getRealUsdBalance() { return realUsdBalance; }
    public void setRealUsdBalance(double realUsdBalance) { this.realUsdBalance = realUsdBalance; }

    public double getExpectedUsdTotal() { return expectedUsdTotal; }
    public void setExpectedUsdTotal(double expectedUsdTotal) { this.expectedUsdTotal = expectedUsdTotal; }

    public boolean isAuthorized() { return authorized; }
    public void setAuthorized(boolean authorized) { this.authorized = authorized; }

    public boolean isStarted() { return started; }
    public void setStarted(boolean started) { this.started = started; }
}

