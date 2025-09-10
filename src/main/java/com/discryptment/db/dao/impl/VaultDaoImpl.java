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
    public boolean setVault(Connection conn, long goldG, int pricePerG, long tgId, String version) throws SQLException {
        String seqSql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM vault";
        int nextId;
        try (PreparedStatement ps = conn.prepareStatement(seqSql)) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            nextId = rs.getInt("next_id");
        }
        String sql = "INSERT INTO vault_state (gold_g, price_per_g, updated_by, version) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, goldG);
            ps.setInt(2, pricePerG);
            ps.setLong(3, tgId);
            ps.setString(4, version);
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
