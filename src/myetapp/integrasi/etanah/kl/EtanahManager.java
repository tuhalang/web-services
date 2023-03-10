package myetapp.integrasi.etanah.kl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import myetapp.db.DbManager;
import myetapp.entities.etanah.Dokumen;
import myetapp.entities.etanah.Hakmilik;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

public class EtanahManager {

	static Logger myLogger = Logger.getLogger("EtanahApplicationResponse");

	public static EtanahApplicationResponse etUpdateEndorsanPPT(
			String transactionID, String noFail, String noJilid, ListData listData) {

		EtanahApplicationResponse result = new EtanahApplicationResponse();

		try {
			if (transactionID != null && transactionID.trim().length() > 0 && !transactionID.trim().equals("?")) {
				if (checkExistTransactionID(transactionID)) {
					if (updateEndorsan(transactionID, noJilid, listData)) {
						result.setCode("0");
						result.setDescription("Update Endorsan Success.");
						result.setDetail("Update Endorsan Success.");
					} else {
						result.setCode("1");
						result.setDescription("Update Endorsan Failed.");
						result.setDetail("Update Endorsan Failed.");
					}
				} else {
					result.setCode("1");
					result.setDescription("Failed.");
					result.setDetail("Transaction not found.");
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

	private static boolean checkExistTransactionID(String transactionID) throws Exception {
		boolean recordExist = false;
		Connection con = null;
		
		try {
			con = DbManager.getInstance().getConnection();
			Statement stmt = con.createStatement();
			String sql = "SELECT 1 FROM INT_PPTPERMOHONAN WHERE ID_PERMOHONAN = '" + transactionID + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				recordExist = true;
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
	
	private static boolean updateEndorsan(String transactionID, String noJilid,
			ListData listData) throws Exception {
		boolean updateRecord = false;
		Connection con = null;
		String sql = "";
		
		try {
			con = DbManager.getInstance().getConnection();
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();

			//UPDATE INT_PPTPERMOHONAN
			sql = "UPDATE INT_PPTPERMOHONAN SET NO_JILID = '" + noJilid + "', TARIKH_TERIMA = SYSDATE, FLAG_ENDORSAN = 'Y' WHERE ID_PERMOHONAN = '" + transactionID + "'";
			stmt.execute(sql);
			
			for (int i = 0; i < listData.getListHakmilik().size(); i++) {
				Hakmilik hakmilik = listData.getListHakmilik().get(i);
				
				if (!hakmilik.getIdHakmilik().equals("?")) {
					sql = "INSERT INTO INT_PPTHAKMILIKENDORSAN ("
							+ " ID_PERMOHONAN, ID_HAKMILIK, ID_HAKMILIK_SAMBUNGAN, KOD_NEGERI, KOD_DAERAH," 
							+ " KOD_MUKIM, KOD_HAKMILIK, NO_HAKMILIK, KOD_LOT, NO_LOT, NO_SEKSYEN,"
							+ " KOD_LUAS_SAMBUNGAN, LUAS_SAMBUNGAN, NO_PERSERAHAN, TARIKH_ENDORSAN, MASA_ENDORSAN)"
							+ " VALUES ("
							+ " '" + transactionID + "','" + hakmilik.getIdHakmilik() + "','" + hakmilik.getIdHakmilikSambungan() + "','" + hakmilik.getKodNegeri() + "','" + hakmilik.getKodDaerah() + "',"
							+ " '" + hakmilik.getKodMukim() + "','" + hakmilik.getKodHakmilik() + "','" + hakmilik.getNoHakmilik() + "','" + hakmilik.getKodLot() + "','" + hakmilik.getNoLot() + "','" + hakmilik.getNoSeksyen() + "',"
							+ " '" + hakmilik.getKodLuasSambungan() + "','" + hakmilik.getLuasSambungan() + "','" + hakmilik.getNoPerserahan() + "','" + hakmilik.getTarikhEndorsan() + "','" + hakmilik.getMasaEndorsan() + "')";
					stmt.execute(sql);
				}				
			}
			
			for (int j = 0; j < listData.getListDokumen().size(); j++) {
				Dokumen dokumen = listData.getListDokumen().get(j);
				
				if (!dokumen.getFlagDokumen().equals("?")) {
					byte[] decodedBytes;
					decodedBytes = new BASE64Decoder().decodeBuffer(dokumen.getDocContent());
					
					PreparedStatement ps = con
							.prepareStatement("INSERT INTO INT_PPTDOKUMENENDORSAN "
									+ " (ID_PERMOHONAN, FLAG_DOKUMEN, NAMA_DOKUMEN, CONTENT)"
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
}
