// 
// Decompiled by Procyon v0.5.36
// 

package com.Server.io;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.HashMap;
import java.sql.Statement;

public class MySQL
{
    protected final int MySQL_ID;
    public Statement stat;
    protected final Object LOCK;
    public static int baseId;
    protected static final Object LOCK_MYSQL;
    public static final int DATABASE_DATA = 0;
    public static final int DATABASE_GAME = 1;
    private static final HashMap<Integer, Connection> conn_map;
    private static final String URL_FORMAT = "jdbc:mysql://%s/%s";
    
    public MySQL(final int connId) throws SQLException {
        this.LOCK = new Object();
        synchronized (MySQL.LOCK_MYSQL) {
            this.MySQL_ID = MySQL.baseId++;
            this.stat = MySQL.conn_map.get(connId).createStatement();
        }
    }
    
    public void close() {
        try {
            synchronized (this.LOCK) {
                this.stat.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection createConnection(final int connId, final String host, final String database, final String user, final String pass) {
        try {
            return MySQL.conn_map.put(connId, DriverManager.getConnection(String.format("jdbc:mysql://%s:3306/%s",host,database), user, pass));
        }
        catch (SQLException e2) {
            e2.printStackTrace();
            System.exit(0);
            return null;
        }
    }
    
    public static Connection getConnection(final int connId) {
        return MySQL.conn_map.get(connId);
    }
    
    public static boolean disconnect(final int connId) {
        try {
            MySQL.conn_map.remove(connId).close();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    static {
        MySQL.baseId = 0;
        LOCK_MYSQL = new Object();
        conn_map = new HashMap<Integer, Connection>();
    }
}
