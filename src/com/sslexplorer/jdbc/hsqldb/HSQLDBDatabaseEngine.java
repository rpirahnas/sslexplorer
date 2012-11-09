package com.sslexplorer.jdbc.hsqldb;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sslexplorer.boot.ContextHolder;
import com.sslexplorer.boot.SystemProperties;
import com.sslexplorer.jdbc.JDBCDatabaseEngine;

public class HSQLDBDatabaseEngine extends JDBCDatabaseEngine {

    private boolean serverMode;
    private File dbDir;

    public HSQLDBDatabaseEngine() {
        super("HSQLDB", "org.hsqldb.jdbcDriver");
        serverMode = "true".equalsIgnoreCase(SystemProperties.get("sslexplorer.hsqldb.tcpipServer"));
        dbDir = ContextHolder.getContext().getDBDirectory();
    }

    public String getURL() {
        if (serverMode) {
            return "jdbc:hsqldb:hsql://127.0.0.1:9001/" + getDatabase();
        } else {
            return "jdbc:hsqldb:file:" + ContextHolder.getContext().getDBDirectory() + "/" + getDatabase();
        }
    }

    public boolean isDatabaseExists() {
        return new File(dbDir, getDatabase() + ".data").exists();
    }

    public String formatTimestamp(Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date(c.getTimeInMillis()));
    }
    
    public void setDBDir(File dbDir) {
        this.dbDir = dbDir;
    }
}
