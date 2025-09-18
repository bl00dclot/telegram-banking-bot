package com.discryptment.service;

import com.discryptment.db.Database;
import com.discryptment.db.dao.ConfigDao;
import com.discryptment.db.dao.UserDao;
import com.discryptment.db.dao.impl.ConfigDaoImpl;
import com.discryptment.db.dao.impl.UserDaoImpl;
import com.discryptment.model.Config;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    private final ConfigDao configDao = new ConfigDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    public boolean verifyPassword(String attempt) throws SQLException {
        try(Connection conn = Database.getConnection()){
            Optional<Config> cfg = configDao.getConfig(conn);
            if(cfg.isEmpty() || cfg.get().getAuthPasswordHash() == null) return false;
            String storedHash = cfg.get().getAuthPasswordHash();
            return BCrypt.checkpw(attempt, storedHash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean setRegistration(boolean status) throws SQLException{
        try(Connection conn = Database.getConnection()){
            boolean result = configDao.setRegistrationStatus(conn, status);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setPassword(String newPassword) throws SQLException{
        String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        try(Connection conn = Database.getConnection()){
            String updateSql = "UPDATE config SET auth_password = ? WHERE id = 1";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setString(1, hash);
                int updated = ps.executeUpdate();
                if (updated > 0) {
                    // updated existing row
                    return;
                }
            }
            // If no row updated, insert row with id=1
            String insertSql = "INSERT INTO config (id, auth_password, admin_id) VALUES (1, ?, NULL)";
            try (PreparedStatement ps2 = conn.prepareStatement(insertSql)) {
                ps2.setString(1, hash);
                ps2.executeUpdate();
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void markUserAuthorizedByTelegramId(long telegramId, boolean authorized) throws SQLException{
        try(Connection conn = Database.getConnection()){
            Integer id = userDao.findIdByTelegramId(conn, telegramId);
            if(id == null){
                return;
            }
            userDao.setAuthorized(conn, id, authorized);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public boolean isAuthorized(long telegramId) throws SQLException{
    	try(Connection conn = Database.getConnection()){
    		return userDao.isAuthorized(conn, telegramId);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    public long getAdmin() throws SQLException{
        try(Connection conn = Database.getConnection()){
            Optional<Config> cfg = configDao.getConfig(conn);
            return cfg.map(Config::getAdminId).orElse(null);
        } catch(Exception e){
            throw new RuntimeException();
        }
    }

}
