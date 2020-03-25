package myetapp.tanah;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Vector;

import lebah.db.SQLRenderer;
import myetapp.db.DbManager;
import myetapp.entities.gis.RequestObjectGIS;
import myetapp.entities.tanah.TanahAgensi;
import myetapp.ws.UpdateApplicationResponse;

import org.apache.log4j.Logger;

public class PengurusanMaklumaTanah {
	static Logger myLog = Logger.getLogger(" myetapp.tanah.PengurusanMaklumaTanah");
	private static UpdateApplicationResponse result = null;

	/**
	 * Senarai Tanah PTP Mengikut Agensi
	 * 
	 * @param requestData
	 * @return
	 */
	public static TanahAgensi[] getListing(RequestObjectGIS requestData ) {
		TanahAgensi[] results = new TanahAgensi[1];
		TanahAgensi result = null;
		Vector<TanahAgensi> res = null;
		try {
			Connection con = DbManager.getInstance().getConnection();			
			res = getInfo(con,requestData);
			myLog.info("getListing:res"+res);
			if(!res.isEmpty())
				results = new TanahAgensi[res.size()];
				myLog.info("getListing:"+res.size());
				if (res.size()!=0) {
				for (int i = 0; i < res.size(); i++) {
					TanahAgensi gis = (TanahAgensi)res.get(i);
					result = new TanahAgensi();
					result.setNama(gis.getNama());
					result.setKod(gis.getKod());
					result.setBil(gis.getBil());
					result.setLuas(gis.getLuas());			
			     	results[i] = result;
					
				}
				//result.setCode("0");
				//result.setDescription("Success");
				//result.setDetail("Successfully update UPI Code");
						 
			} else {
				result = new TanahAgensi();
				result.setCode("1");
				result.setDescription("Failed");
				result.setDetail("TIADA FAIL");
				results[0] = result;
			
			}
			con.close();
					
		} catch(Exception ex) {
			result = new TanahAgensi();
			result.setCode("1");
			result.setDescription("Failed.");
			result.setDetail("ex:"+ex.getMessage());
			results[0] = result;
			
		}
		return results;
		
	}

	private static Vector<TanahAgensi> getInfo(Connection con, RequestObjectGIS updateData) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Vector<TanahAgensi> vec = new Vector<TanahAgensi>();
	   	SQLRenderer r = new SQLRenderer();
	    r.add("ID_NEGERI");
	    r.add("NAMA_NEGERI");
	  	r.add("BIL_LOT_MILIK");
	    r.add("LUAS_MILIK");
	  	r.add("BIL_LOT_RIZAB");
	   	r.add("LUAS_RIZAB");
	    String sql = r.getSQLSelect("VHTP_REKOD_RIN_NEG ");
	   	myLog.info("getInfo: sql = " + sql);
  	
  		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			TanahAgensi mt = null;
			while(rs.next()) {
				int i=1;
				mt = new TanahAgensi();
				mt.setNama(rs.getString("NAMA_NEGERI"));
				mt.setKod(rs.getString("ID_NEGERI"));
				mt.setBil(rs.getInt("BIL_LOT_MILIK"));
				mt.setLuas(rs.getDouble("LUAS_MILIK"));
				vec.addElement(mt);
	
			}
			stmt.close();	
			
		} catch (SQLException e) {
			myLog.debug("insertResult: " + e.getMessage());
			e.printStackTrace();
		} 
		return vec;
		
	}
	

}
