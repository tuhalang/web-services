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
	private static Integration iSek4 =null;
	private static Integration iSek8 =null;
	private static Integration iBorangB =null;
	private static Integration iBorangD =null;
	private static Integration iBorangK =null;
	
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
	
	private static boolean updateEndorsan(String transactionID, Permohonan permohonan) throws Exception {
		boolean updateRecord = false;
		Connection con = null;
		String sql = "";
		
		try {
			con = DbManager.getInstance().getConnection();
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();

			//UPDATE INT_PPTPERMOHONAN
			sql = "UPDATE TBLINTANAHPERMOHONAN SET "+
				" NO_JILID = '" + permohonan.getNoJilid() + "'" +
				" , TARIKH_TERIMA = SYSDATE" +
				" , FLAG_ENDORSAN = 'Y' " +
				" WHERE NO_PERMOHONAN = '" + transactionID + "'";
			stmt.execute(sql);
			
			for (int i = 0; i < permohonan.getListHakmilik().size(); i++) {
				Hakmilik hakmilik = permohonan.getListHakmilik().get(i);
				
				if (!hakmilik.getIdHakmilik().equals("?")) {
					sql = "INSERT INTO TBLINTANAHPERMOHONANMILIKEND ("
							+ " NO_PERMOHONAN, ID_HAKMILIK, ID_HAKMILIK_SAMBUNGAN, KOD_NEGERI, KOD_DAERAH," 
							+ " KOD_MUKIM, KOD_HAKMILIK, NO_HAKMILIK, KOD_LOT, NO_LOT, NO_SEKSYEN,"
							+ " KOD_LUAS_SAMBUNGAN, LUAS_SAMBUNGAN, NO_PERSERAHAN, TARIKH_ENDORSAN, MASA_ENDORSAN)"
							+ " VALUES ("
							+ " '" + transactionID + "','" + hakmilik.getIdHakmilik() + "','" + hakmilik.getIdHakmilikSambungan() + "','" + hakmilik.getKodNegeri() + "','" + hakmilik.getKodDaerah() + "',"
							+ " '" + hakmilik.getKodMukim() + "','" + hakmilik.getKodHakmilik() + "','" + hakmilik.getNoHakmilik() + "','" + hakmilik.getKodLot() + "','" + hakmilik.getNoLot() + "','" + hakmilik.getNoSeksyen() + "',"
							+ " '" + hakmilik.getKodLuasSambungan() + "','" + hakmilik.getLuasSambungan() + "','" + hakmilik.getNoPerserahan() + "','" + hakmilik.getTarikhEndorsan() + "','" + hakmilik.getMasaEndorsan() + "')";
					stmt.execute(sql);
				}				
			}
			
			for (int j = 0; j < permohonan.getListDokumen().size(); j++) {
				Dokumen dokumen = permohonan.getListDokumen().get(j);
				
				if (!dokumen.getFlagDokumen().equals("?")) {
					byte[] decodedBytes;
					decodedBytes = new BASE64Decoder().decodeBuffer(dokumen.getDocContent());
					
					PreparedStatement ps = con
							.prepareStatement("INSERT INTO INT_PPTDOKUMENENDORSAN "
									+ " (ID_RUJUKAN, FLAG_DOKUMEN, NAMA_DOKUMEN, CONTENT)"
									+ "VALUES(?,?,?,?)");

					ps.setString(1, transactionID);
					ps.setString(2, dokumen.getFlagDokumen());
					ps.setString(3, dokumen.getNamaDokumen());
					ps.setBytes(4, decodedBytes);
					ps.executeUpdate();
				}				
			}
			
			con.commit();
			updateRecord = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}  finally {
			if (con != null)
				con.close();
		}
		
		return updateRecord;
	}
	
	public static TanahApplicationResponse kemaskiniPermohonan(Permohonan permohonan) throws Exception {
		TanahApplicationResponse result = new TanahApplicationResponse();
		
		if(permohonan.getJenis().equals("A")){	
			if (getSek4().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update MMK (sek4) Success.");
				
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Update MMK (sek4) Failed.");
				
			}
		}else if(permohonan.getJenis().equals("B")){
			if (getSek4().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update MMK (sek4) Success.");
				
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("Update MMK (sek4) Failed.");
				
			}	
		}else if(permohonan.getJenis().equals("C")){
			if (getSek4().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
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
			if (getSek4().kemaskiniPermohonan(idPermohonan,idTransaksi,permohonan)) {
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
			if(permohonan.getTarikh()==null){
				
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
	
	private static Integration getSek8(){
		if(iSek8==null){
			iSek8 = new Sek8Bean();
		}
		return iSek8;
				
	}
	private static Integration getSek4(){
		if(iSek4==null){
			iSek4 = new MMKSek4Bean();
		}
		return iSek4;
				
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
//	private static Integration getSek4(){
//		if(iSek4==null){
//			iSek4 = new MMKSek4Bean();
//		}
//		return iSek4;
//				
//	}
	

}
