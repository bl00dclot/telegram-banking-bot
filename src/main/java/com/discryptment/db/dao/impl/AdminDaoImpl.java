package com.discryptment.db.dao.impl;

import com.discryptment.db.dao.AdminDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AdminDaoImpl implements AdminDao {
    public AdminDaoImpl(){};

    @Override
    public boolean isAdmin(Connection conn, long TelegramId) throws SQLException {
        return false;
    }

    @Override
    public void addAdmin(Connection conn, long actorTgId, String label) throws SQLException {

    }

    @Override
    public void removeAdmin(Connection conn, long TelegramId) throws SQLException {

    }

    @Override
    public void initialAdmin(Connection conn, long TelegramId) throws SQLException {
        String sql = "INSERT INTO admins (telegram_id, label, created_by) " +
                "SELECT ?, ?, ? " +
                "WHERE NOT EXISTS (SELECT 1 FROM admins)";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, TelegramId);
            ps.setString(2, "initial-admin");
            ps.setLong(3, 0);
            ps.executeUpdate();
        }
    }

    @Override
    public Set<Long> listAdmins(Connection conn) throws SQLException {
        String sql = "SELECT telegram_id FROM admins";
        Set<Long> result = new HashSet<>();
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                result.add(rs.getLong(1));
            }
            return result;
        }
    }
}
