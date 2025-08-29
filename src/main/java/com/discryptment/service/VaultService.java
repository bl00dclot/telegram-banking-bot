package com.discryptment.service;

import com.discryptment.db.Database;
import com.discryptment.db.dao.VaultDao;

import java.sql.Connection;

public class VaultService {
    private final VaultDao vaultDao;
    public VaultService(VaultDao vaultDao){
        this.vaultDao = vaultDao;
    }
    public boolean setVault(int usd, int gold){
        try(Connection conn = Database.getConnection()){
            return vaultDao.setVault(conn, usd, gold);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
