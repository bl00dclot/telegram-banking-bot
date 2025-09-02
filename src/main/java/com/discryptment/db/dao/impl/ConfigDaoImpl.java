package com.discryptment.db.dao.impl;

import com.discryptment.db.dao.ConfigDao;
import com.discryptment.model.Config;

import java.sql.*;
import java.util.Optional;

public class ConfigDaoImpl implements ConfigDao {
    public ConfigDaoImpl() {
    }

    @Override
    public boolean isRegistrationOpen(Connection conn) throws SQLException {
        String sql = "SELECT registration_open FROM config WHERE id = 1 LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean("registration_open");
            }
            // default to false if config row missing
            return false;
        }
    }

    public boolean setRegistrationStatus(Connection conn, boolean status) throws SQLException {
        String sql = "UPDATE config SET registration_open = ? WHERE id = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            int rows = ps.executeUpdate();
            return rows == 1;
        }

    }

    @Override
    public Optional<Config> getConfig(Connection conn) throws SQLException {
        String sql = "SELECT id, auth_password, admin_id FROM config LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Config cfg = new Config();
                cfg.setId(rs.getInt("id"));
                cfg.setAuthPasswordHash(rs.getString("auth_password"));
                long admin = rs.getLong("admin_id");
                if (rs.wasNull()) cfg.setAdminId(admin);
                return Optional.of(cfg);
            }
            return Optional.empty();
        }
    }

    @Override
    public void upsertConfig(Connection conn, Config cfg) throws SQLException {
        String update = "UPDATE config SET auth_password = ?, admin_id = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(update)) {
            ps.setString(1, cfg.getAuthPasswordHash());
            if (cfg.getAdminId() == null) ps.setNull(2, Types.BIGINT);
            else ps.setLong(2, cfg.getAdminId());
            ps.setObject(3, cfg.getId() == null ? 1 : cfg.getId());
            int updated = ps.executeUpdate();
            if (updated == 0) {
                String insert = "INSERT INTO config (id, auth_password, admin_id) VALUE(?, ?, ?)";
                try (PreparedStatement ins = conn.prepareStatement(insert)) {
                    ins.setInt(1, cfg.getId() == null ? 1 : cfg.getId());
                    ins.setString(2, cfg.getAuthPasswordHash());
                    if (cfg.getAdminId() == null) ins.setNull(3, Types.BIGINT);
                    else ins.setLong(3, cfg.getAdminId());
                }
            }
        }
    }


}
