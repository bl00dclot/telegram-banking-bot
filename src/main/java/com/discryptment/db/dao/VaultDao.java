package com.discryptment.db.dao;

import com.discryptment.model.Vault;
import okhttp3.Connection;

import java.sql.SQLException;

// VaultDao.java
public interface VaultDao {
    Vault getVault(Connection conn) throws SQLException;
    void setVault(Connection conn, double usd, double gold) throws SQLException;
    boolean tryWithdrawUsd(Connection conn, double amount) throws SQLException; // atomic update
    boolean tryWithdrawGold(Connection conn, double amount) throws SQLException; // atomic update
}
