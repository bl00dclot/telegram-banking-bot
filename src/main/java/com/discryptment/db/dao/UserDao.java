package com.discryptment.db.dao;
import com.discryptment.model.User;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.List;

// UserDao.java
public interface UserDao {
    void addUser(Connection conn, User user);
    Integer findIdByTelegramId(Connection conn, long telegramId) throws SQLException;
    User findById(Connection conn, int id) throws SQLException;
    User findByTelegramId(Connection conn, long telegramId) throws SQLException;
    int createUser(Connection conn, User user) throws SQLException; // returns created id
    boolean updateBalances(Connection conn, int userId, double deltaGold, double deltaRealUsd, double deltaExpectedUsd) throws SQLException;
    void setAuthorized(Connection conn, int userId, boolean authorized) throws SQLException;
    List<User> listAll(Connection conn) throws SQLException;
}

