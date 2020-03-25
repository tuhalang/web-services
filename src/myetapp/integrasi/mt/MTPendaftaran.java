package myetapp.integrasi.mt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import myetapp.db.DbManager;
import myetapp.entities.mt.Registration;
import myetapp.ws.UpdateApplicationResponse;

import org.apache.log4j.Logger;

public class MTPendaftaran {
	static Logger myLog = Logger.getLogger("myetapp.integrasi.mt.MTPendaftaran");

	/**
	 *  Main function to update Case No. received from MAHKAMAH
	 * @param Registration
	 * @return
	 */
	public static UpdateApplicationResponse updateRegistrationCase(Registration reg) {
		UpdateApplicationResponse result = new UpdateApplicationResponse();
		String transactionID = reg.getTransactionID();
		//String petitionNo = reg.getPetitionNo();

		try {
			if (isTransaction(transactionID)==true) {
				result.setCode("1");
				result.setDescription("Failed");
				result.setDetail("TransactionID already exist.");
				
			} else {
				//myLog.info("isTransaction("+transactionID+",)");
				boolean isKemaskini = kemaskiniPendaftaran(reg);
				if (isKemaskini) {
					result.setCode("0");
					result.setDescription("Success");
					result.setDetail("Successfully update Case No.");
						
				} else {
					result.setCode("1");
					result.setDescription("Failed");
					result.setDetail("Unable to update Case No.");
						
				}
			}
			
		} catch (Exception ex) {
			result.setCode("1");
			result.setDescription("Failed.");
			result.setDetail(ex.getMessage());
			
		}
		return result;
		
	}
	
	private static boolean kemaskiniPendaftaran(Registration reg) throws Exception {
			boolean isSuccess = false;
			Connection con = DbManager.getInstance().getConnection();
			
			// Update flag_rep in table permohonan
			//boolean kemaskiniPendaftaran = updateFlagRepPermohonan(con, updateData);
			//if (kemaskiniPendaftaran) {
				// Insert new record in table keputusan
				//boolean successInsertResult = insertResult(con, updateData, kaveat, holder, document);
				boolean kemaskiniPendaftaran = kemaskiniPendaftaran(con, reg);
				if (kemaskiniPendaftaran) {
					try {
						con.commit();
						isSuccess = true;
						
					} catch (SQLException e) {
						try {
							con.rollback();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					} finally {
						try {
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			//}
			return isSuccess;
			
		}
	
	private static boolean kemaskiniPendaftaran(Connection con,Registration reg) {
		boolean isSuccess = false;
		String sql = "UPDATE TBLINTMTPENDAFTARAN SET " +
				" NO_KES = '"+reg.getCaseNo()+"' " +
				" ,TARIKH_KES=TO_TIMESTAMP('"+ reg.getRegistrationDate() + "','YYYY-MM-DD HH24:MI:SS.FF3')"+
				" ,NO_BILIK='"+reg.getRoom()+"'"+	
				" ,CATATAN='"+reg.getRemarks()+"'"+
				" ,ID_TRANSAKSI='"+reg.getTransactionID()+"'"+
				" WHERE " +
				" ID_PENDAFTARANSTR = '"+ reg.getRefID() + "'";
		myLog.info("updateFlagRepPermohonan(con,"+reg+"),sql="+sql);

		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			isSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccess;
		
	}
	
	private static boolean isTransaction(String transactionID) {
		boolean isExist = false;
		Connection con = DbManager.getInstance().getConnection();
		String sql = "SELECT 1 FROM TBLINTMTPENDAFTARAN T " +
				"WHERE T.ID_PENDAFTARAN = '"+ transactionID + "'";
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				isExist = true;
			}
			stmt.close();
			//myLog.info("isTransactionExist("+transactionID+"), isExist="+isExist);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isExist;
		
	}
	
	
}
