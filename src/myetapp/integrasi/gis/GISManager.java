package myetapp.integrasi.gis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Vector;

import lebah.db.SQLRenderer;
import myetapp.db.DbManager;
import myetapp.entities.gis.RequestObjectGIS;
import myetapp.entities.gis.Tanah;
import myetapp.ws.UpdateApplicationResponse;
import org.apache.log4j.Logger;



//import ekptg.helpers.DB;

public class GISManager {

	static Logger myLog = Logger.getLogger(" myetapp.integrasi.gis.GISManager");
	private static UpdateApplicationResponse result = null;
	
	/**
	 * Kemaskini maklumat latitude,longLatitude
	 * 
	 * @param requestData
	 * @return
	 */
	public static UpdateApplicationResponse updateResult(
			RequestObjectGIS requestData) {
		result = new UpdateApplicationResponse();
		try {
			Connection con = DbManager.getInstance().getConnection();
			// insert new updated result.
			boolean updateResult = insertResult(con, requestData);
			if (updateResult) {
				result.setCode("0");
				result.setDescription("Success");
				result.setDetail("Successfully update UPI Code");

			} else {
				result.setCode("1");
				result.setDescription("Failed");
				result.setDetail("Unable to update UPI Code.");

			}
			con.close();

		} catch (Exception ex) {
			result.setCode("1");
			result.setDescription("Failed.");
			result.setDetail(ex.getMessage());
		}
		return result;

	}
	/**
	 * Maklumat lain bagi Tanah PTP
	 * @param requestData
	 * @return
	 */
	public static Tanah updateResultInfo(RequestObjectGIS requestData ) {
		Tanah result = new Tanah();
		try {
			Connection con = DbManager.getInstance().getConnection();
			boolean updateResult = insertResultInfo(con,requestData);
			Tanah gis = null;
			gis = getInfo(con,requestData,requestData.getNoFail());
			myLog.info("getTanah:gis"+gis);
			if(gis != null){
				result.setNoFail(gis.getNoFail());
				result.setKegunaan(gis.getKegunaan());
				result.setTarikhDaftar(gis.getTarikhDaftar());
				result.setKodNegeri(gis.getKodNegeri());
				result.setKodDaerah(gis.getKodDaerah());
				result.setKodMukim(gis.getKodMukim());
				result.setKodSeksyen(gis.getKodSeksen());
				result.setLuas(gis.getLuas());
				result.setLuasKeterangan(gis.getLuasKeterangan());
				result.setKodLot(gis.getKodLot());
				result.setNoLot(gis.getNoLot());
				result.setJenisHakmilik(gis.getJenisHakmilik());
				result.setNoHakmilik(gis.getNoHakmilik());
				//result.setCharter(rs.getInt(17));
				result.setKodAgensi(gis.getKodAgensi());
				result.setKodKementerian(gis.getKodKementerian());
				result.setStatus(gis.getStatus());
			
			} else {
				result = new Tanah();
				result.setCode("1");
				result.setDescription("Failed");
				result.setDetail("TIADA FAIL");
				
			}
			con.close();
						
		} catch(Exception ex) {
			result = new Tanah();
			result.setCode("1");
			result.setDescription("Failed.");
			result.setDetail("ex:"+ex.getMessage());
				
		}
		return result;
		
	}

	/**
	 * Senarai fail,UPI belum dibuat Charting
	 * 
	 * @param requestData
	 * @return
	 */
	public static Tanah[] getListing(RequestObjectGIS requestData ) {
		Tanah[] results = new Tanah[1];
		Tanah result = null;
		Vector<Tanah> res = null;
		myLog.info("request data : " + requestData);

		try {	
			Connection con = DbManager.getInstance().getConnection();
			res = getInfo(con,requestData);
			myLog.info("getListing:"+res.size());
			
			if(!res.isEmpty()){
				results = new Tanah[res.size()];
				if (res.size()!=0) {
					for (int i = 0; i < res.size(); i++) {
						Tanah gis = (Tanah)res.get(i);
						result = new Tanah();
						result.setNoFail(gis.getNoFail());
						result.setKegunaan(gis.getKegunaan());
						result.setTarikhDaftar(gis.getTarikhDaftar());
						result.setKodNegeri(gis.getKodNegeri());
						result.setKodDaerah(gis.getKodDaerah());
						result.setKodMukim(gis.getKodMukim());
						result.setKodSeksyen(gis.getKodSeksen());
						result.setLuas(gis.getLuas());
						result.setLuasKeterangan(gis.getLuasKeterangan());
						result.setKodLot(gis.getKodLot());
						result.setNoLot(gis.getNoLot());
						result.setJenisHakmilik(gis.getJenisHakmilik());
						result.setNoHakmilik(gis.getNoHakmilik());
						//result.setCharter(rs.getInt(17));
						result.setKodAgensi(gis.getKodAgensi());
						result.setKodKementerian(gis.getKodKementerian());
						result.setStatus(gis.getStatus());
						result.setUPI(gis.getUPI());
						results[i] = result;
					}
				}else{
					myLog.info("res size : 0");
				}
				//result.setCode("0");
				//result.setDescription("Success");
				//result.setDetail("Successfully update UPI Code");
						 
			} else {
				result = new Tanah();
				result.setCode("1");
				result.setDescription("Failed");
				result.setDetail("TIADA FAIL");
				results[0] = result;
			
			}
			con.close();
					
		} catch(Exception ex) {
			result = new Tanah();
			result.setCode("1");
			result.setDescription("Failed.");
			result.setDetail("ex:"+ex.getMessage());
			results[0] = result;
			myLog.info("error : " + ex.getMessage());
			ex.printStackTrace();
		}
		return results;
		
	}

	private static boolean insertResult(Connection con,RequestObjectGIS updateData) {
		boolean isSuccess = false;
		SQLRenderer r = new SQLRenderer();
		if(!updateData.getStatus().equals("2") && !updateData.getStatus().equals("3"))
			r.update("UPI", updateData.getUPICode());
		
		r.update("NO_FAIL", updateData.getNoFail());
		r.update("STATUS_TANAH", updateData.getStatus());
		// r.add("TARIKH_TERIMA",r.unquote("TO_TIMESTAMP('" +
		// updateData.getCreatedDate() + "','YYYY-MM-DD HH24:MI:SS.FF3')))"));
		r.add("LATITUDE", updateData.getLatitude());
		r.add("LONGITUDE", updateData.getLongLatitude());
		r.add("ID_CHARTER", updateData.getCreatedID());
		String sql = r.getSQLUpdate("TBLINTGIS");
		myLog.info("insertResult : sql = " + sql);
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			isSuccess = true;
			stmt.close();

		} catch (SQLException e) {
			myLog.debug("insertResult: " + e.getMessage());
			e.printStackTrace();
		}
		return isSuccess;

	}

	private static boolean insertResultInfo(Connection con,RequestObjectGIS updateData) {
		boolean isSuccess = false;
		SQLRenderer r = new SQLRenderer();
		r.add("UPI", updateData.getUPICode());
		r.add("NO_FAIL", updateData.getNoFail());
		r.add("REMARKS", "Successfully get Info");
		String sql = r.getSQLInsert("TBLINTGIS");
		// myLog.debug("insertResultInfo: sql = " + sql);
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			isSuccess = true;
			stmt.close();

		} catch (SQLException e) {
			myLog.debug("insertResult: " + e.getMessage());
			e.printStackTrace();
		}
		return isSuccess;

	}

	private static Vector<Tanah> getInfo(Connection con, RequestObjectGIS updateData) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Vector<Tanah> vec = new Vector<Tanah>();
	   	SQLRenderer r = new SQLRenderer();
	    r.add("NO_FAIL");
	    r.add("TUJUAN");
	  	r.add("TARIKH_DAFTAR");
	    r.add("KOD_NEGERI");
	    //r.add("NAMA_NEGERI");
	  	r.add("KOD_DAERAH");
	    r.add("KOD_MUKIM");
	    r.add("SEK");
	  	r.add("LUAS");
	    r.add("KETERANGAN_LUAS");
	    r.add("KOD_LOT");
	  	r.add("NO_LOT");
	    r.add("KOD_HAKMILIK");
	    r.add("NO_HAKMILIK");
	  	//r.add("LATITUDE");	  
	    //r.add("LONGITUDE");
	    r.add("ID_MASUK");
	  	r.add("KOD_AGENSI");	  	
	  	r.add("KOD_KEMENTERIAN");	  	
	  	r.add("STATUS_TANAH");	  	
	  	r.add("UPI");	  	
	  	//r.add("KETERANGAN_STATUSTANAH");	  	
	    String sql = r.getSQLSelect("VGIS_SENARAI_CHARTING ");
	   	myLog.info("getSQLSelect >>> VGIS SENARAI CHARTING : " + sql);
  		Statement stmt;
		
		try {
			stmt = con.createStatement();
			sql += " WHERE LATITUDE = 'TIADA'";
	   		myLog.info("getSQLSelect >>> VGIS SENARAI CHARTING and latitude : " + sql);
			ResultSet rs = stmt.executeQuery(sql);
	   		myLog.info("Execute sukses.");
	   		// myLog.info("rs size : " +  rs.length());
	   		myLog.info("rs data : " +  rs);
			//RequestObjectGIS gisData = null;
			Tanah mt = null;

			myLog.info("accessing loop...");
			int i=1;

			while(rs.next()) {
				myLog.info("i : " + i);
				//gisData = new RequestObjectGIS();
				mt = new Tanah();
				mt.setNoFail(rs.getString("NO_FAIL"));
				mt.setKegunaan(rs.getString("TUJUAN"));
				mt.setTarikhDaftar(rs.getString("TARIKH_DAFTAR").equals("")?rs.getString(3):sdf.format(rs.getDate("TARIKH_DAFTAR")));
				mt.setKodNegeri(rs.getString("KOD_NEGERI"));
				mt.setKodDaerah(rs.getString("KOD_DAERAH"));
				mt.setKodMukim(rs.getString("KOD_MUKIM"));
				mt.setKodSeksyen(rs.getString("SEK"));
				mt.setLuas(rs.getDouble("LUAS"));
				mt.setLuasKeterangan(rs.getString("KETERANGAN_LUAS"));
				mt.setKodLot(rs.getString("KOD_LOT"));
				mt.setNoLot(rs.getString("NO_LOT"));
				mt.setJenisHakmilik(rs.getString("KOD_HAKMILIK"));
				mt.setNoHakmilik(rs.getString("NO_HAKMILIK"));
				//myLog.info("NO_HAKMILIK="+rs.getString("NO_HAKMILIK"));
				//
				//
				mt.setCharter(rs.getInt("ID_MASUK"));
				mt.setKodAgensi(rs.getString("KOD_AGENSI"));
				mt.setKodKementerian(rs.getString("KOD_KEMENTERIAN"));
				mt.setStatus(rs.getString("STATUS_TANAH"));
				mt.setUPI(rs.getString("UPI"));
				//myLog.info("STATUS_TANAH="+rs.getString("STATUS_TANAH"));
				vec.addElement(mt);
				i++;
			}
			myLog.info("exit loop...");
			stmt.close();	
			myLog.info("statement closed...");
			
		} catch (SQLException e) {
			myLog.debug("insertResult : " + e.getMessage());
			e.printStackTrace();
		} 
		myLog.info("DATA VECTOR : " + vec);
		return vec;
		
	}
	
	private static Tanah getInfo(Connection con, RequestObjectGIS updateData,String noFail) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Vector<Tanah> vec = new Vector<Tanah>();
	   	SQLRenderer r = new SQLRenderer();
	    r.add("NO_FAIL");
	    r.add("TUJUAN");
	  	r.add("TARIKH_DAFTAR");
	    r.add("KOD_NEGERI");
	    //r.add("NAMA_NEGERI");
	  	r.add("KOD_DAERAH");
	    r.add("KOD_MUKIM");
	    r.add("SEK");
	  	r.add("LUAS");
	    r.add("KETERANGAN_LUAS");
	    r.add("KOD_LOT");
	  	r.add("NO_LOT");
	    r.add("KOD_HAKMILIK");
	    r.add("NO_HAKMILIK");
	  	//r.add("LATITUDE");	  
	    //r.add("LONGITUDE");
	    r.add("ID_MASUK");
	  	r.add("KOD_AGENSI");	  	
	  	r.add("KOD_KEMENTERIAN");	  	
	  	r.add("STATUS_TANAH");	  	
	  	//r.add("KETERANGAN_STATUSTANAH");
	  	r.add("NO_FAIL",noFail);
	  	r.add("STATUS_TANAH",updateData.getStatus());	  	
	   	myLog.info("getInfo: UPI = " + updateData.getUPICode());
	  	if(!updateData.getUPICode().equals(""))
	  		r.add("UPI",updateData.getUPICode());
	  		
	    String sql = r.getSQLSelect("VGIS_SENARAI_CHARTING ");
	   	myLog.debug("getSQLSelect: sql = " + sql);
  	
  		Statement stmt;
		Tanah mt = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				mt = new Tanah();
				mt.setNoFail(rs.getString("NO_FAIL"));
				mt.setKegunaan(rs.getString("TUJUAN"));
				mt.setTarikhDaftar(rs.getString("TARIKH_DAFTAR").equals("")?rs.getString(3):sdf.format(rs.getDate("TARIKH_DAFTAR")));
				mt.setKodNegeri(rs.getString("KOD_NEGERI"));
				mt.setKodDaerah(rs.getString("KOD_DAERAH"));
				mt.setKodMukim(rs.getString("KOD_MUKIM"));
				mt.setKodSeksyen(rs.getString("SEK"));
				mt.setLuas(rs.getDouble("LUAS"));
				mt.setLuasKeterangan(rs.getString("KETERANGAN_LUAS"));
				mt.setKodLot(rs.getString("KOD_LOT"));
				mt.setNoLot(rs.getString("NO_LOT"));
				mt.setJenisHakmilik(rs.getString("KOD_HAKMILIK"));
				mt.setNoHakmilik(rs.getString("NO_HAKMILIK"));
				//
				//
				mt.setCharter(rs.getInt("ID_MASUK"));
				mt.setKodAgensi(rs.getString("KOD_AGENSI"));
				mt.setKodKementerian(rs.getString("KOD_KEMENTERIAN"));
				mt.setStatus(rs.getString("STATUS_TANAH"));
			  	//r.add("KETERANGAN_STATUSTANAH");	
				//vec.addElement(mt);
	
			}
			stmt.close();	
			
		} catch (SQLException e) {
			myLog.debug("insertResult: " + e.getMessage());
			e.printStackTrace();
		} 
		return mt;
		
	}

}
