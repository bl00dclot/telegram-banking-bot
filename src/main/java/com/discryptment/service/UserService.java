package com.discryptment.service;

import com.discryptment.db.Database;
import com.discryptment.db.dao.ConfigDao;
import com.discryptment.db.dao.UserDao;
import com.discryptment.model.User;
import com.discryptment.service.exceptions.RegistrationClosedException;

import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private final UserDao userDao;
    private final ConfigDao configDao;

    public UserService(UserDao userDao, ConfigDao configDao) {
        this.userDao = userDao;
        this.configDao = configDao;
    }


    public void registerUser(User user) {
        User existing = null;
        try (Connection conn = Database.getConnection()) {

            existing = userDao.findByTelegramId(conn, user.getTelegramId());

            if (existing != null) {

                int concurrent = userDao.findIdByTelegramId(conn, user.getTelegramId());
                User inputtedUserName = userDao.findById(conn, concurrent);
                System.out.println("In DB there is a user - " + inputtedUserName.getUsername());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (existing == null) {
            try (Connection conn = Database.getConnection()) {
                //check for registration status
                boolean open = configDao.isRegistrationOpen(conn);
                if (open) {
                    userDao.createUser(conn, user);
                    System.out.println("A new user '" + user.getTelegramId() + "' will be created ");
                } else {
                    throw new RegistrationClosedException("Registration is currently closed.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public User getProfile(User user) {
        User profile;
        try (Connection conn = Database.getConnection()) {
            profile = userDao.findByTelegramId(conn, user.getTelegramId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return profile;
    }

    public boolean addGold(int userId, int gold) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            return userDao.updateGoldBalance(conn, userId, gold);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//    public User getUserByTelegramId(long tgId) {
//        return userDao.findByTelegramId(conn);
//    }
}

