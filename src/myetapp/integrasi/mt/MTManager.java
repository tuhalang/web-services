package myetapp.integrasi.mt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lebah.db.Db;
import myetapp.db.DbManager;
import myetapp.entities.mt.Document;
import myetapp.entities.mt.Holder;
import myetapp.entities.mt.Kaveat;
import myetapp.helpers.DB;
import myetapp.ws.UpdateApplicationResponse;

import org.apache.log4j.Logger;

//import sun.misc.BASE64Decoder;

public class MTManager {

	static Logger myLog = Logger.getLogger("myetapp.integrasi.mt.MTManager");

	/**
	 *  Main function to process FORM C result received from MAHKAMAH
	 * @param requestData
	 * @param kaveat
	 * @param holder
	 * @param document
	 * @return
	 */
	public static UpdateApplicationResponse updateCFormResult(
			RequestObject requestData, Kaveat kaveat, Holder holder,
			Document document) {

		UpdateApplicationResponse result = new UpdateApplicationResponse();
		String transactionID = requestData.getTransactionID();
		String petitionNo = requestData.getPetitionNo();

		try {
			myLog.info("CHECKPOINT---------- INTEGRATION CHECK DATA");
			if (isTransactionExist(transactionID)==true) {
				result.setCode("1");
				result.setDescription("Failed");
				result.setDetail("TransactionID already exist.");
				
			} else {
				myLog.info("isTransactionExist("+requestData+",)");
				//System.out.println("masuk sini");
				if (isPetitionExist(petitionNo)==true) {	
					boolean isInsert = insertFormCResult(requestData, kaveat, holder, document);
					if (isInsert) {
						result.setCode("0");
						result.setDescription("Success");
						result.setDetail("Successfully insert new Form C result.");
						
					} else {
						result.setCode("1");
						result.setDescription("Failed");
						result.setDetail("Unable to insert Form C result.");
						
					}
				}else {
					result.setCode("1");
					result.setDescription("Failed.");
					result.setDetail("Unknown petition number.");
					// result.setCode("0");
					// result.setDescription("Success");
					// result.setDetail("Successfully insert new Form C result.");
					
				}
			}
		} catch (Exception ex) {
			result.setCode("1");
			result.setDescription("Failed.");
			result.setDetail(ex.getMessage());
			
		}
		return result;
		
	}

	private static boolean insertFormCResult(RequestObject updateData,Kaveat kaveat, Holder holder, Document document) 
		throws Exception {
		boolean isSuccess = false;
		Connection con = DbManager.getInstance().getConnection();
		
		// Update flag_rep in table permohonan
		boolean successUpdatePermohonan = updateFlagRepPermohonan(con, updateData);
		if (successUpdatePermohonan) {
			// Insert new record in table keputusan
			boolean successInsertResult = insertResult(con, updateData, kaveat, holder, document);
			if (successInsertResult) {
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
		}
		return isSuccess;
		
	}

	private static boolean insertResult(
		Connection con,RequestObject updateData, Kaveat kaveat, Holder holder,Document document) 
		throws Exception {
		boolean isSuccess = false;
		String sqlKaveat = "";
		String sqlHolder = "";
		myLog.info("insertResult(con,"+updateData+",kavea,holder,document)");

		// insert TBLINTMTKEPUTUSAN
		saveData(updateData, document);
		String appliedDate = "";
		if (kaveat != null) {
			if (kaveat.getPetitionNo() != null) {
				if (kaveat.getPetitionNo().length() > 0) {

					if (kaveat.getAppliedDate() != null) {
						appliedDate = kaveat.getAppliedDate();
						appliedDate = appliedDate.replace("T", " ");
					}

					sqlKaveat = "INSERT INTO TBLINTMTBRGCKAVEAT"
							+ " (IDBRGCADAKAVEAT, IDKEPUTUSAN, NOKAVEAT,"
							+ " TARIKHKAVEAT, NAMAPENGKAVEAT, NAMAFIRMA,"
							+ " IDKADBIRU, JENISTRANSAKSI, FLAG_REP)"
							+ " VALUES " + " ('" + lebah.template.UID.getUID()
							+ "', " + " '" + updateData.getTransactionID()
							+ "', " + " '" + kaveat.getPetitionNo() + "',"
							+ " TO_TIMESTAMP('" + appliedDate
							+ "','YYYY-MM-DD HH24:MI:SS.FF3')," + " '"
							+ kaveat.getApplicantName() + "'," + " '"
							+ kaveat.getRepresentative() + "', " + " '"
							+ updateData.getBlueCardID() + "', 'I', '3')";
				}
			}
		}

		String caseFilingDate = "";
		if (holder != null) {
			if (holder.getPetitionNo() != null) {
				if (holder.getPetitionNo().length() > 0) {

					if (holder.getCaseFilingDate() != null) {
						caseFilingDate = holder.getCaseFilingDate();
						caseFilingDate = caseFilingDate.replace("T", " ");
					}

					sqlHolder = "INSERT INTO TBLINTMTBRGCFAIL"
							+ " (IDBRGCFAIL, IDKEPUTUSAN, NOFAILAWAL,"
							+ " TARIKHPERMOHONANAWAL, NAMAPEMOHONAWAL, NAMAPEJAGENSI,"
							+ " IDKADBIRU, JENISTRANSAKSI, FLAG_REP)"
							+ "VALUES" + "('" + lebah.template.UID.getUID()
							+ "', '" + updateData.getTransactionID() + "', '"
							+ holder.getPetitionNo() + "'," + "TO_TIMESTAMP('"
							+ caseFilingDate
							+ "','YYYY-MM-DD HH24:MI:SS.FF3'), '"
							+ holder.getApplicantName() + "', '"
							+ holder.getPetitioner() + "'," + "'"
							+ updateData.getBlueCardID() + "', 'I', '3')";
				}
			}
		}

		Statement stmt;
		try {
			stmt = con.createStatement();

			if (kaveat != null) {
				if (kaveat.getPetitionNo() != null) {
					if (kaveat.getPetitionNo().length() > 0) {
						stmt.executeUpdate(sqlKaveat);
					}
				}
			}
			if (holder != null) {
				if (holder.getPetitionNo() != null) {
					if (holder.getPetitionNo().length() > 0) {
						stmt.executeUpdate(sqlHolder);
					}
				}
			}
			isSuccess = true;
			stmt.close();

			if (updateData.getFormType().equals("WH")
					|| updateData.getFormType().equals("YL")) {
				updateToTablePermohonanPPK(con, updateData, holder, kaveat);
			}

		} catch (SQLException e) {
			//myLogger.debug("insertResult: " + e.getMessage());
			e.printStackTrace();
		}
		return isSuccess;
	}

	private static void saveData(RequestObject updateData, Document document) throws Exception {
		Db db = null;
		try {
			db = new Db();
			Connection con = db.getConnection();
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			
			//Kemaskini table TBLINTMTKEPUTUSAN, DEACTIVATE KEPUTUSAN YANG SEBELUM
			String sql = "UPDATE TBLINTMTKEPUTUSAN SET FLAG_AKTIF = 'T' WHERE IDKADBIRU = '" + updateData.getBlueCardID() + "'";
			myLog.info("saveData("+updateData+",document),sqlupdate="+sql);
			stmt.execute(sql);
			con.commit();
			
			//Kemasukan maklumat table TBLINTMTKEPUTUSAN
			byte[] decodedBytes;
			myLog.info("decodedBytes="+document.getDocContent());

			//decodedBytes = new BASE64Decoder().decodeBuffer(document.getDocContent());// tarik field yg simpan stringbase64
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO TBLINTMTKEPUTUSAN " +
							" (ID_TRANSAKSI, KEPUTUSANBORANGC,TARIKHJANABORANGC, IDKADBIRU, JENISTRANSAKSI, FLAG_REP, CATATAN " +
							//" ,DOCID,DOCTYPE, CONTENT,JENIS_MIME, TARIKH_TERIMA,FLAG_AKTIF" +
							") " +
						    " VALUES (?,?,TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS.FF3'),?,?,?,?" +
						    //",?,?,?,?,SYSDATE,?" +
						    ")");
			myLog.info("saveData("+updateData+",document),sqlinsert="+ps.toString());
			String statementText = ps.toString();
			String query = statementText.substring( statementText.indexOf( ": " ) + 2 );
			myLog.info("saveData("+updateData+",document),sqlinsert query="+query);

			ps.setString(1, updateData.getTransactionID());
			ps.setString(2, updateData.getFormType());
			ps.setString(3, updateData.getFormCreatedDate());
			ps.setString(4, updateData.getBlueCardID());
			ps.setString(5, "I");
			ps.setString(6, "3");
			ps.setString(7, updateData.getRemarks());
//			ps.setString(8, document.getDocID());
//			ps.setString(9, document.getDocType());// NAMA_FAIL (VARCHAR2)
//			ps.setBytes(10, decodedBytes);// CONTENT (BLOB)
//			ps.setString(11, "application/pdf");// JENIS_MIME (VARCHAR2)
//			ps.setString(12, "Y");// TAMBAH TEMPORARY			
			myLog.info("saveData("+updateData+",document),sqlinsert="+updateData.getTransactionID());
			//ps.executeUpdate();

			con.commit();
		} finally {
			if (db != null)
				db.close();
		}

	}

	// TODO - UPDATE FILE PERMOHONAN BASED ON INTERGRATION DATA RECEIVED
	private static boolean updateToTablePermohonanPPK(Connection con,
			RequestObject updateData, Holder holder, Kaveat kaveat)
			throws Exception {
		boolean isSuccess = false;
		String formType = "";
		String sqlKaveat = "";
		String sqlgetDataKeputusan = "";
		String sqlHolder = "";
		Db db = null;
		Statement stmt;
		ResultSet rs = null;
		String ID_PERMOHONAN = "";
		String ID_KEPUTUSANPERMOHONAN = "";

		stmt = con.createStatement();

		if (updateData.getFormType().equals("WH")) {
			formType = "P";
		} else if (updateData.getFormType().equals("YL")) {
			formType = "K";
		}
		String sql = "UPDATE TBLPPKKEPUTUSANPERMOHONAN "
				+ "SET TARIKH_TERIMA_BORANGC=TO_TIMESTAMP('"
				+ updateData.getFormCreatedDate()
				+ "','YYYY-MM-DD HH24:MI:SS.FF3'), " + "JENIS_BORANGC='"
				+ formType + "' WHERE ID_PERMOHONAN IN "
				+ "(SELECT C.ID_PERMOHONAN " + "FROM TBLPFDFAIL A, "
				+ "TBLPPKKEPUTUSANPERMOHONAN B, " + "TBLPPKPERMOHONAN C "
				+ "WHERE B.ID_PERMOHONAN = C.ID_PERMOHONAN "
				+ "AND C.ID_FAIL = A.ID_FAIL " + "AND A.NO_FAIL ='"
				+ updateData.getPetitionNo() + "')";

		// aishah

		sqlgetDataKeputusan = " SELECT C.ID_PERMOHONAN, B.ID_KEPUTUSANPERMOHONAN "
				+ "FROM TBLPFDFAIL A, "
				+ "TBLPPKKEPUTUSANPERMOHONAN B, "
				+ "TBLPPKPERMOHONAN C "
				+ "WHERE B.ID_PERMOHONAN = C.ID_PERMOHONAN "
				+ "AND C.ID_FAIL = A.ID_FAIL "
				+ "AND A.NO_FAIL ='"
				+ updateData.getPetitionNo() + "'";

		rs = stmt.executeQuery(sqlgetDataKeputusan);

		while (rs.next()) {
			ID_PERMOHONAN = rs.getString("ID_PERMOHONAN");
			ID_KEPUTUSANPERMOHONAN = rs.getString("ID_KEPUTUSANPERMOHONAN");
		}

		long idKaveatPeguam = DB.getNextID("TBLPPKKAVEATPEGUAM_SEQ");
		if (kaveat != null) {
			if (kaveat.getPetitionNo() != null) {
				if (kaveat.getPetitionNo().length() > 0) {

					sqlKaveat = "INSERT INTO TBLPPKKAVEATPEGUAM "
							+ " (ID_KAVEATPEGUAM, NO_KAVEAT, "
							+ " TARIKH_KAVEAT, NAMA_KAVEAT, NAMA_FIRMA, ID_PERMOHONAN ) "
							+ " SELECT " + idKaveatPeguam + " , NOKAVEAT, "
							+ " TARIKHKAVEAT , NAMAPENGKAVEAT, NAMAFIRMA, "
							+ ID_PERMOHONAN + "  "
							+ " FROM TBLINTMTBRGCKAVEAT  "
							+ " WHERE IDKADBIRU = '"
							+ updateData.getBlueCardID() + "'";
				}
			}
		}

		if (holder != null) {
			if (holder.getPetitionNo() != null) {
				if (holder.getPetitionNo().length() > 0) {
					sqlHolder = " UPDATE tblppkpermohonan "
							+ " SET NO_FAIL_AWAL="
							+ holder.getPetitionNo()
							+ ", "
							+ "NAMA_PEMOHON_AWAL='"
							+ holder.getApplicantName()
							+ "',"
							+ " TARIKH_KEMASKINI = TO_CHAR(SYSDATE, 'DD/MM/YYYY') "
							+ " WHERE ID_PERMOHONAN = '" + ID_PERMOHONAN + "' ";
				}
			}
		}

		try {

			stmt.executeUpdate(sql);
			stmt.executeUpdate(sqlgetDataKeputusan);

			if (kaveat != null) {
				if (kaveat.getPetitionNo() != null) {
					if (kaveat.getPetitionNo().length() > 0) {
						stmt.executeUpdate(sqlKaveat);
					}
				}
			}
			if (holder != null) {
				if (holder.getPetitionNo() != null) {
					if (holder.getPetitionNo().length() > 0) {
						stmt.executeUpdate(sqlHolder);
					}
				}
			}
			rs.close();
			stmt.close();

			isSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	private static boolean updateFlagRepPermohonan(Connection con,RequestObject updateData) {
		boolean isSuccess = false;
		String sql = "UPDATE TBLINTMTPERMOHONAN SET FLAG_REP = '3' " +
				" WHERE " +
				" PETISYENNO = '"+ updateData.getPetitionNo()+ "' " +
				" AND IDKADBIRU = '"+ updateData.getBlueCardID() + "'";
		myLog.info("updateFlagRepPermohonan(con,"+updateData+"),sql="+sql);

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

	private static boolean isPetitionExist(String petitionNo) {
		boolean isExist = false;
		Connection con = DbManager.getInstance().getConnection();
		String sql = "SELECT 1 FROM TBLINTMTPERMOHONAN WHERE PETISYENNO = '"
				+ petitionNo + "'";
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				isExist = true;
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}				

		myLog.info("isPetitionExist("+petitionNo+"), isExist="+isExist);
		return isExist;
		
		
	}

	private static boolean isTransactionExist(String transactionID) {
		boolean isExist = false;
		Connection con = DbManager.getInstance().getConnection();
		String sql = "SELECT 1 FROM TBLINTMTKEPUTUSAN T WHERE T.IDKEPUTUSAN = '"
				+ transactionID + "'";
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				isExist = true;
			}
			stmt.close();
			myLog.info("isTransactionExist("+transactionID+"), isExist="+isExist);
			//System.out.println("sql+++++ "+isExist);

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
