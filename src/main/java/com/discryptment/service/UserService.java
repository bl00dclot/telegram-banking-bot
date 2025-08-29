package com.discryptment.service;

import com.discryptment.db.Database;
import com.discryptment.db.dao.UserDao;
import com.discryptment.model.User;
import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    

    public void registerUser(User user) {
        User existing = null;
        try(Connection conn = Database.getConnection()){
            existing = userDao.findByTelegramId(conn, user.getTelegramId());
            if(existing != null){
                System.out.println("In DB there should be " + existing);
            }
             } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (existing == null) {
            try(Connection conn = Database.getConnection()){
                userDao.createUser(conn, user);
                System.out.println("A new user will be created " + user.getTelegramId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public User getProfile(User user){
        User profile;
        try(Connection conn = Database.getConnection()){
            profile = userDao.findByTelegramId(conn, user.getTelegramId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
            return profile;
        }
    public boolean addGold(int userId, int gold) throws SQLException {
        try(Connection conn = Database.getConnection()){
            return userDao.updateGoldBalance(conn, userId, gold);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


//    public User getUserByTelegramId(long tgId) {
//        return userDao.findByTelegramId(conn);
//    }
}

