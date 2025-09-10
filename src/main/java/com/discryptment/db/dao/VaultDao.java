package com.discryptment.db.dao;

import com.discryptment.model.Vault;

import java.sql.*;

// VaultDao.java
public interface VaultDao {
    Vault getVault(Connection conn) throws SQLException;
    boolean setVault(Connection conn, long goldG, int pricePerG, long tgId, String version) throws SQLException;
    boolean tryWithdrawUsd(Connection conn, double amount) throws SQLException; // atomic update
    boolean tryWithdrawGold(Connection conn, int amount) throws SQLException; // atomic update
}
