package com.discryptment.db.dao;

import com.discryptment.model.Invoice;
import okhttp3.Connection;

import java.sql.SQLException;
import java.util.List;

// InvoiceDao.java
public interface InvoiceDao {
    String insertInvoice(Connection conn, Invoice invoice) throws SQLException; // returns id (String or int)
    List<Invoice> findByUserId(Connection conn, int userId) throws SQLException;
}
