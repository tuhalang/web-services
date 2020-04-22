package myetapp.integrasi.etanah;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import myetapp.db.DbManager;
import myetapp.entities.etanah.Dokumen;
import myetapp.entities.etanah.Hakmilik;
import myetapp.integrasi.Integration;
import sun.misc.BASE64Decoder;

import org.apache.log4j.Logger;

public class MMKSek4Bean implements Integration {
	private static Logger myLog = Logger.getLogger(MMKSek4Bean.class);
	private static SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
	//private Db db = null;
	private Connection con = null;		
	private String sql = "";

	@Override
	public boolean kemaskiniPermohonan(String idPermohonan,String transactionID, Permohonan permohonan) throws Exception{
		boolean isSucces = true;
		
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
				//COMMENT ON COLUMN TBLINTANAHDOKUMENEND.FLAG_DOKUMEN IS '1=Borang D; 2=Warta Kerajaan Persekutuan; 3=Surat Iringan Borang D; 4=Jadual Bearing Distance & Coordinate; 5=Pelan Pengambilan Digital; 6=Surat Maklum; 7=Borang K; 8=Surat Iringan Borang K; 9=Borang D; 10=Warta Borang D; 11=Surat Iringan Batalkan Endorsan Borang D; 12=Warta Tarik Balik; 13=Surat Iringan Tarik Balik;';

				if (!dokumen.getFlagDokumen().equals("?")) {
					byte[] decodedBytes;
					decodedBytes = new BASE64Decoder().decodeBuffer(dokumen.getDocContent());
					
					PreparedStatement ps = con.prepareStatement("INSERT INTO TBLINTANAHDOKUMENEND "
									+ " (ID_PERMOHONAN, FLAG_DOKUMEN, NAMA_DOKUMEN, CONTENT)"
									+ "VALUES(?,?,?,?)");

					ps.setString(1, transactionID);
					ps.setString(2, dokumen.getFlagDokumen());
					ps.setString(3, dokumen.getNamaDokumen());
					ps.setBytes(4, decodedBytes);
					ps.executeUpdate();
				}				
			}
			
			kemaskiniMMK(idPermohonan,permohonan,stmt);

			con.commit();
			isSucces = true;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}  finally {
			if (con != null)
				con.close();
		}
		return isSucces;
		
	}
	
	private String getMMK(String idPermohonan) throws Exception {
		boolean recordExist = false;
		Connection con = null;
		String idMMK = "";
		try {
			con = DbManager.getInstance().getConnection();
			Statement stmt = con.createStatement();
			String sql = "SELECT ID_MMK FROM tblpptmmk WHERE ID_PERMOHONAN = '" + idPermohonan + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				recordExist = true;
				idMMK =rs.getString("ID_MMK");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}  finally {
			if (con != null)
				con.close();
		}
		
		return idMMK;
	}
	
	public void kemaskiniMMK(String idPermohonan,Permohonan permohonan,Statement stmt) throws Exception{
		 //String tarikhBukafail = "to_date('" + (String)data.get("tarikh_Bukafail") + "','dd/MM/yyyy')";

		sql = "update tblpptmmk set "
			+ "no_rujmmk = "+permohonan.getTarikh()+""
			+ ",tarikh_mmk = to_date('"+permohonan.getTarikh()+"','dd/MM/yyyy')"
			+ " where id_permohonan='"+idPermohonan+"'"
			+ "";
		stmt.execute(sql);

		sql = "update tblpptmmkkeputusan set "
			+ " status_keputusan = "+permohonan.getKeputusan()+""
			+ " ,ulasan = '"+permohonan.getCatatan()+"'"
			+ " ,tarikh_terima = SYSDATE "
			+ " where id_mmk='"+getMMK(idPermohonan)+"'"
			+ "";
		stmt.execute(sql);

	}
	
}
