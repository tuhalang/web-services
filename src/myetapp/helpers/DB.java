package myetapp.helpers;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;

import lebah.db.Db;
import myetapp.engine.StateLookup;

import org.apache.log4j.Logger;

public class DB extends EkptgCache implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5809670899489908145L;
	static Logger myLogger = Logger.getLogger(DB.class);

	public synchronized static long getNextID(String seqName) throws Exception {
		Db db = null;
		// original
		// String sql = "select " + seqName + ".NEXTVAL FROM DUAL ";

		// Get State code from dbconnection.properties
		String statecode = StateLookup.getInstance().getTitle("StateCode");
		String sql = "select " + statecode + " || to_char(sysdate,'YY') || "
				+ seqName + ".NEXTVAL  FROM DUAL ";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getLong(1);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (db != null)
				db.close();
		}

	}

}// close class