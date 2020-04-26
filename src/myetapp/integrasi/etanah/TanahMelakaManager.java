package myetapp.integrasi.etanah;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import myetapp.db.DbManager;
import myetapp.entities.etanah.Dokumen;
import myetapp.entities.etanah.Hakmilik;
import myetapp.integrasi.Integration;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

public class TanahMelakaManager {

	static Logger myLog = Logger.getLogger("myetapp.integrasi.etanah.TanahMelakaManager");
	public static String idPermohonan = "";
	public static String idTransaksi = "";
	private static Integration iBorangB =null;
	private static Integration iBorangD =null;
	private static Integration iBorangK =null;
	private static Integration immk =null;
	private static Integration iPenarikan =null;
	private static Integration iPU =null;
	private static Integration iSek4 =null;
	private static Integration iSek8 =null;
	private static Integration iSijil =null;
	TanahApplicationResponse result = null;
	
	public static TanahApplicationResponse UpdateEndorsan(String idpermohonan,Permohonan listData) {
		TanahApplicationResponse result = new TanahApplicationResponse();
		try {
			if (idpermohonan != null && idpermohonan.trim().length() > 0 && !idpermohonan.trim().equals("?")) {
				idTransaksi = idpermohonan;
				
				if (checkExistPermohonanID(idTransaksi)) {
					
					if (listData.getJenis() != null && listData.getJenis().length() > 0 && !listData.getJenis().trim().equals("?")) {
						result = kemaskiniPermohonan(listData);

//					if (updateEndorsan(idPermohonan, listData)) {
//						result.setCode("0");
//						result.setDescription("Success.");
//						result.setDetail("Update Endorsan Success.");
//					} else {
//						result.setCode("1");
//						result.setDescription("Failed.");
//						result.setDetail("Update Endorsan Failed.");
//					}

					}else {
						result.setCode("1");
						result.setDescription("Failed.");
						result.setDetail("Jenis not found.");
						
					}
					
				} else {
					result.setCode("1");
					result.setDescription("Failed.");
					result.setDetail("Id Permohonan not found.");
					//result.setDetail("Transaction not found.");
				}
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Transaction ID is null.");
			}
		} catch (Exception ex) {
			result.setCode("1");
			result.setDescription("Failed.");
			result.setDetail(ex.getMessage());
		}
		return result;
		
	}	
	
	
	public static TanahApplicationResponse kemaskiniBorangA(String idpermohonan,Permohonan permohonan) {
		TanahApplicationResponse result = new TanahApplicationResponse();
		setResult(result);
		
		try {
			if (idpermohonan != null && idpermohonan.trim().length() > 0 && !idpermohonan.trim().equals("?")) {
				idTransaksi = idpermohonan;
				
				if (checkExistPermohonanID(idTransaksi)) {
					
					if(permohonan.getJenis().equals("A")){	
						result = getSek4().semakanPermohonan(idpermohonan, idTransaksi, permohonan);
					}else if(permohonan.getJenis().equals("B")){
						result = getB().semakanPermohonan(idpermohonan, idTransaksi, permohonan);
					}else if(permohonan.getJenis().equals("C")){
						result = getSek8().semakanPermohonan(idpermohonan, idTransaksi, permohonan);
					}else if(permohonan.getJenis().equals("D")){
						result = getD().semakanPermohonan(idpermohonan, idTransaksi, permohonan);
					}else if(permohonan.getJenis().equals("K")){
						result = getK().semakanPermohonan(idpermohonan, idTransaksi, permohonan);
					}else if(permohonan.getJenis().equals("PD")){
						result = getPen().semakanPermohonan(idpermohonan, idTransaksi, permohonan);
					}else if(permohonan.getJenis().equals("S")){
						result = getSijil().semakanPermohonan(idpermohonan, idTransaksi, permohonan);
					}else if(permohonan.getJenis().equals("PU")){
						result = getPU().semakanPermohonan(idpermohonan, idTransaksi, permohonan);
					}else if(permohonan.getJenis().equals("M2")){
						result = getMMK().semakanPermohonan(idpermohonan, idTransaksi, permohonan);
					}
					
				} else {
//					result.setCode("1");
//					result.setDescription("Failed.");
					result.setDetail("Id Permohonan not found.");

				}
			} else {
//				result.setCode("1");
//				result.setDescription("Failed.");
				result.setDetail("Transaction ID is null.");
			}
		} catch (Exception ex) {
//			result.setCode("1");
//			result.setDescription("Failed.");
			result.setDetail(ex.getMessage());
		}
		return result;
		
	}

	private static boolean checkExistPermohonanID(String transactionID) throws Exception {
		boolean recordExist = false;
		Connection con = null;
		
		try {
			con = DbManager.getInstance().getConnection();
			Statement stmt = con.createStatement();
			String sql = "SELECT ID_PERMOHONAN FROM TBLINTANAHPERMOHONAN WHERE NO_PERMOHONAN = '" + transactionID + "'";
			//String sql = "SELECT 1 FROM INT_PPTPERMOHONAN WHERE ID_PERMOHONAN = '" + transactionID + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				recordExist = true;
				idPermohonan =rs.getString("ID_PERMOHONAN");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}  finally {
			if (con != null)
				con.close();
		}
		
		return recordExist;
	}
	
	public static TanahApplicationResponse kemaskiniPermohonan(Permohonan permohonan) throws Exception {
		TanahApplicationResponse result = new TanahApplicationResponse();
		
		if(permohonan.getJenis().equals("A")){	
			if (getMMK().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update MMK (sek4) Success.");
				
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Update MMK (sek4) Failed.");
				
			}
		}else if(permohonan.getJenis().equals("B")){
			if (getMMK().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update MMK (sek4) Success.");
				
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Update MMK (sek4) Failed.");
				
			}	
		}else if(permohonan.getJenis().equals("C")){
			if (getMMK().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
			//if (updateEndorsan(idPermohonan, permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update MMK (sek8) Success.");
				
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Update MMK (sek8) Failed.");
				
			}
		}else if(permohonan.getJenis().equals("D")){
			if (getD().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
			//if (updateEndorsan(idPermohonan, permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update Borang D Success.");
				
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Update Borang D Failed.");
				
			}

		}else if(permohonan.getJenis().equals("K")){
			if (getK().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
			//if (updateEndorsan(idPermohonan, permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update Borang K Success.");
				
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Update Borang K Failed.");
				
			}

		}else if(permohonan.getJenis().equals("PD")){
			if (getMMK().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
			//if (updateEndorsan(idPermohonan, permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update Penarikan Success.");
				
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Update Penarikan Failed.");
				
			}

		}else if(permohonan.getJenis().equals("S")){
			if (getSek8().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
			//if (updateEndorsan(idPermohonan, permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update Pengecualian Ukur Success.");
				
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Update Pengecualian Failed.");
				
			}

		}else if(permohonan.getJenis().equals("PU")){
			
			result.setCode("1");
			result.setDescription("Failed.");
			//
			if(permohonan.geTarikh()!=null){
				
				if (getSek8().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
					result.setCode("0");
					result.setDescription("Success.");
					result.setDetail("Update Permohonan Ukur Success.");
				
				}else{					
					result.setDetail("Update Permohonan Ukur Failed.");

				}
				
			} else {
//				result.setCode("1");
//				result.setDescription("Failed.");
				result.setDetail("Date Can't be Empty.");

			}

		}else {
			result.setCode("1");
			result.setDescription("Failed.");
			result.setDetail("Jenis not match.");	

		}
		return result;
		
	}
	
	private static void setResult(TanahApplicationResponse result){
		//result = new TanahApplicationResponse();
		result.setCode("1");
		result.setDescription("Failed.");
		//return result;
	
	}
	
	private static Integration getPen(){
		if(iPenarikan==null){
			iPenarikan = new PenarikanBean();
		}
		return iPenarikan;
				
	}
	
	private static Integration getSijil(){
		if(iSijil==null){
			iSijil = new SijilBean();
		}
		return iSijil;
				
	}
	
	private static Integration getPU(){
		if(iPU==null){
			iPU = new PUBean();
		}
		return iPU;
				
	}
	
	private static Integration getSek8(){
		if(iSek8==null){
			iSek8 = new Sek8Bean();
		}
		return iSek8;
				
	}
	private static Integration getMMK(){
		if(immk==null){
			immk = new MMKBean();
		}
		return immk;
				
	}
	private static Integration getB(){
		if(iBorangB==null){
			iBorangB = new BorangBean();
		}
		return iBorangB;
				
	}
	private static Integration getD(){
		if(iBorangD==null){
			iBorangD = new BorangDBean();
		}
		return iBorangD;
				
	}
	private static Integration getK(){
		if(iBorangK==null){
			iBorangK = new BorangKBean();
		}
		return iBorangK;
				
	}
	private static Integration getSek4(){
		if(iSek4==null){
			iSek4 = new Sek4Bean();
		}
		return iSek4;
				
	}
	

}
