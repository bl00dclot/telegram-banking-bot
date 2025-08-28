package com.discryptment.db.dao;

import com.discryptment.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    public UserDaoImpl() {
    }
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setTelegramId(rs.getLong("telegram_id"));
        u.setUsername(rs.getString("username"));
        u.setGoldBalance(rs.getDouble("gold_balance"));
        u.setRealUsdBalance(rs.getDouble("real_usd_balance"));
        u.setExpectedUsdTotal(rs.getDouble("expected_usd_total"));
        u.setAuthorized(rs.getBoolean("authorized"));
        return u;
    }
    @Override
    public Integer findIdByTelegramId(Connection conn, long telegramId) throws SQLException {
        String sql = "SELECT id FROM users WHERE telegram_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, telegramId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
            return null;
        }
    }

    @Override
    public User findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
            return null;
        }
    }

    @Override
    public User findByTelegramId(Connection conn, long telegramId) throws SQLException {
        String sql = "SELECT * FROM users WHERE telegram_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, telegramId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
            return null;
        }
    }

    @Override
    public int createUser(Connection conn, User user) throws SQLException {
        // generate id manually (SELECT COALESCE(MAX(id),0)+1)
        String seqSql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM users";
        int nextId;
        try (PreparedStatement ps = conn.prepareStatement(seqSql)) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            nextId = rs.getInt("next_id");
        }

        String sql = "INSERT INTO users (id, telegram_id, username, gold_balance, real_usd_balance, expected_usd_total, authorized) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nextId);
            ps.setLong(2, user.getTelegramId());
            ps.setString(3, user.getUsername());
            ps.setDouble(4, user.getGoldBalance());
            ps.setDouble(5, user.getRealUsdBalance());
            ps.setDouble(6, user.getExpectedUsdTotal());
            ps.setBoolean(7, user.isAuthorized());
            ps.executeUpdate();
        }
        return nextId;
    }

    @Override
    public boolean updateBalances(Connection conn, int userId, double deltaGold, double deltaRealUsd, double deltaExpectedUsd) throws SQLException {
        String sql = "UPDATE users SET gold_balance = gold_balance + ?, real_usd_balance = real_usd_balance + ?, expected_usd_total = expected_usd_total + ? WHERE id = ? AND gold_balance + ? >= 0 AND real_usd_balance + ? >= 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, deltaGold);
            ps.setDouble(2, deltaRealUsd);
            ps.setDouble(3, deltaExpectedUsd);
            ps.setInt(4, userId);
            ps.setDouble(5, deltaGold);
            ps.setDouble(6, deltaRealUsd);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }
    @Override
    public boolean updateGoldBalance(Connection conn, int userId, int deltaGold) throws SQLException {
        String sql = "UPDATE users SET gold_balance = gold_balance + ? " +
                "WHERE id = ? AND gold_balance + ? >= 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deltaGold);
            ps.setInt(2, userId);
            ps.setInt(3, deltaGold);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }
    @Override
    public boolean updateRealUsdBalance(Connection conn, int userId, double deltaRealUsd) throws SQLException {
        String sql = "UPDATE users SET real_usd_balance = real_usd_balance + ? " +
                "WHERE id = ? AND real_usd_balance + ? >= 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, deltaRealUsd);
            ps.setInt(2, userId);
            ps.setDouble(3, deltaRealUsd);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }
    @Override
    public boolean updateExpectedUsdBalance(Connection conn, int userId, double deltaExpectedUsd) throws SQLException {
        String sql = "UPDATE users SET expected_usd_total = expected_usd_total + ? " +
                "WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, deltaExpectedUsd);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows == 1;
        }
    }


    @Override
    public void setAuthorized(Connection conn, int userId, boolean authorized) throws SQLException {
        String sql = "UPDATE users SET authorized = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, authorized);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<User> listAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users";
        List<User> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) out.add(mapUser(rs));
        }
        return out;
    }
}
