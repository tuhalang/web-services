package myetapp.integrasi.etanah;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
//import java.text.SimpleDateFormat;
import myetapp.db.DbManager;
import myetapp.entities.etanah.Dokumen;
import myetapp.entities.etanah.Hakmilik;
import myetapp.integrasi.Integration;
import sun.misc.BASE64Decoder;

import org.apache.log4j.Logger;

public class BorangKBean implements Integration {
	private static Logger myLog = Logger.getLogger(BorangKBean.class);
	//private static SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
	//private Db db = null;
	private Connection con = null;		
	private String sql = "";
	TanahApplicationResponse result = null;

	public TanahApplicationResponse semakanPermohonan(String idPermohonan,String transactionID,  Permohonan permohonan) throws Exception {
		result = new TanahApplicationResponse();
		setResult(result);
		//String noFail = permohonan.getNoFail();
		String noWarta = permohonan.getNoWarta();
		String tarikh = permohonan.getTarikhWarta();
		//String keputusan = permohonan.getKeputusan();
		//String ulasan = permohonan.getCatatan();
		
		if (noWarta == null || noWarta.trim().length() == 0 || noWarta.trim().equals("?")) {
			result.setDetail("Sila Isi No. Warta.");
		}else if(tarikh == null || tarikh.trim().length() == 0 || tarikh.trim().equals("?")){	
			result.setDetail(" Sila Isi Tarikh Warta.");
		
//		}else if(ulasan == null || ulasan.trim().length() == 0 || ulasan.trim().equals("?")){	
//			result.setDetail("Ulasan Can't be Empty.");
		}else{		
			if (kemaskiniPermohonan(idPermohonan,transactionID,permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update Borang K Success.");
				
			} else {
				result.setDetail("Update Borang K Failed.");

			}
		}
		return  result;
		
	}
	
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
			//stmt.execute(sql);
			
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
			
			kemaskiniPermohonani(transactionID,permohonan,stmt);

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
	
	public void kemaskiniPermohonani(String noPermohonan,Permohonan permohonan,Statement stmt) throws Exception{
		sql = "insert into tblintanahppt (no_permohonan,tarikh_keputusan,keputusan,catatan,flag_urusan,tarikh_terima,tarikh_masuk) " +
				"values ("+
				" '"+noPermohonan+"'" +
				" ,to_date('"+permohonan.getTarikhWarta()+"','dd/MM/yyyy')" +
				" ,'"+permohonan.getNoWarta()+"'" +
				" ,'"+permohonan.getCatatan()+"'" +
				" ,'K',SYSDATE,SYSDATE "+
				")";

		myLog.info("sql2="+sql);
		stmt.execute(sql);

	}
	
	private static void setResult(TanahApplicationResponse result){
		result.setCode("1");
		result.setDescription("Failed.");
	
	}
	
}
