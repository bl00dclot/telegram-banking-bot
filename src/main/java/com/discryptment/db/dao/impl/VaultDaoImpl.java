package com.discryptment.db.dao.impl;

import com.discryptment.db.dao.VaultDao;
import com.discryptment.model.Vault;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VaultDaoImpl implements VaultDao {
    @Override
    public Vault getVault(Connection conn) throws SQLException {
        return null;
    }

    @Override
    public boolean setVault(Connection conn, double usd, int gold) throws SQLException {
        String seqSql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM vault";
        int nextId;
        try (PreparedStatement ps = conn.prepareStatement(seqSql)) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            nextId = rs.getInt("next_id");
        }
        String sql = "INSERT INTO vault (id, gold) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nextId);
            ps.setInt(2, gold);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }

    @Override
    public boolean tryWithdrawUsd(Connection conn, double amount) throws SQLException {
        return false;
    }

    @Override
    public boolean tryWithdrawGold(Connection conn, int amount) throws SQLException {
        return false;
    }
}
