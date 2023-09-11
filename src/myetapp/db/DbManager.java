package myetapp.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import lebah.db.Db;
import lebah.db.DbException;

import org.apache.log4j.Logger;

// Manage Database Connection
public class DbManager {

	private static DbManager dbInstance;
	static ResourceBundle rb = null;
	static Logger myLogger = Logger.getLogger("DBManager");

	private DbManager() {
	}

	// Create single connection pool
	public static DbManager getInstance() {
		if (dbInstance == null) {
			dbInstance = new DbManager();
		}
		return dbInstance;
	}

	// get new connection pool
	public Connection getConnection() {
		Db db = null;
		Connection con = null;
		try {
			db = new Db();
		} catch (DbException e) {
			myLogger.info("getConnection: " + e.getMessage());
			e.printStackTrace();
		}
		con = db.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e) {
			myLogger.info("getConnection2: " + e.getMessage());
			e.printStackTrace();
		}
		return con;
	}
}