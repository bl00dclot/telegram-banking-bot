package com.discryptment.db.dao;

import com.discryptment.model.Config;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.Optional;

// ConfigDao.java
public interface ConfigDao {
    Optional<Config> getConfig(Connection conn) throws SQLException;
    void upsertConfig(Connection conn, Config cfg) throws SQLException;
    boolean isRegistrationOpen(Connection conn) throws SQLException;
    boolean setRegistrationStatus(Connection conn, boolean status) throws SQLException;
}
