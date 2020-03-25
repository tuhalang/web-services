package myetapp.utils;

import java.sql.Connection;
import java.sql.Statement;

import lebah.db.Db;
import lebah.db.SQLRenderer;

public class IntLogManager {

	public static void recordLogMT(String noFail, String jenisTransaksi, String flagIntergrasi, String flagSuccess, String catatan) {
		
		Db db = null;
		Connection conn = null;
		String sql = "";
		
		try {
			db = new Db();
			conn = db.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
						
			// TBLINTMTLOG
			r.add("NO_FAIL", noFail);
			r.add("JENIS_TRANSAKSI", jenisTransaksi);
			r.add("FLAG_INTERGRASI", flagIntergrasi);
			r.add("TARIKH_TRANSAKSI", r.unquote("SYSDATE"));
			r.add("FLAG_TRANSAKSI", flagSuccess);	
			r.add("CATATAN", catatan);	
			sql = r.getSQLInsert("TBLINTMTLOG");
			stmt.executeUpdate(sql);
			
			conn.commit();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	
}
