package com.discryptment.service;

import com.discryptment.db.Database;
import com.discryptment.db.dao.AdminDao;
import com.discryptment.db.dao.impl.AdminDaoImpl;

import java.sql.Connection;
import java.util.Set;

public class AdminService {
    private final AdminDao adminDao = new AdminDaoImpl();

    public boolean initAdmin(long tgId){
        try(Connection conn = Database.getConnection()){
            adminDao.initialAdmin(conn, tgId);
            Set<Long> adminList = adminDao.listAdmins(conn);
            if(!adminList.isEmpty()){
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
