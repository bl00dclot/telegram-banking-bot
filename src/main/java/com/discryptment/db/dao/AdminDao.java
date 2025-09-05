package com.discryptment.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public interface AdminDao {
    boolean isAdmin(Connection conn, long TelegramId) throws SQLException;
    void addAdmin(Connection conn, long actorTgId, String label) throws SQLException;
    void removeAdmin(Connection conn, long TelegramId) throws SQLException;
    Set<Long> listAdmins(Connection conn) throws SQLException;
    void initialAdmin(Connection conn, long TelegramId) throws SQLException;
}
