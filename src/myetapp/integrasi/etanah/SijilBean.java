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

//import org.apache.log4j.Logger;

public class SijilBean implements Integration {
	//private static Logger myLog = Logger.getLogger(SijilBean.class);
	//private static SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
	//private Db db = null;
	private Connection con = null;		
	private String sql = "";
	private static ITanah iPer=null;
	TanahApplicationResponse result = null;

	public TanahApplicationResponse semakanPermohonan(String idPermohonan,String transactionID,  Permohonan permohonan) throws Exception {
		result = new TanahApplicationResponse();
		getPer().setResult(result);
		//String noFail = permohonan.getNoFail();
		//String noJilid = permohonan.getNoJilid();
		String tarikh = permohonan.getTarikh();
		//String keputusan = permohonan.getKeputusan();
		String pu = permohonan.getNoPU();
		/*
		if (noFail == null || noFail.trim().length() == 0 || noFail.trim().equals("?")) {
			result.setDetail("File No. Can't be Empty.");

		} else if(noJilid == null || noJilid.trim().length() == 0 || noJilid.trim().equals("?")){
			result.setDetail("Jilid No. Can't be Empty.");
				
		} else if(keputusan == null || keputusan.trim().length() == 0 || keputusan.trim().equals("?")){
			result.setDetail("Keputusan Can't be Empty.");
		}else */
		if(tarikh == null || tarikh.trim().length() == 0 || tarikh.trim().equals("?")){	
			result.setDetail("Sila Isi Tarikh.");
		
		}else if(pu == null || pu.trim().length() == 0 || pu.trim().equals("?")){	
			result.setDetail("Sila Isi No. PU");
		}else{		
			if (kemaskiniPermohonan(idPermohonan,transactionID,permohonan)) {
				result.setCode("0");
				result.setDescription("Success.");
				result.setDetail("Update Permohonan Pengecualian Ukur Success.");
				
			} else {
				result.setDetail("Update Permohonan Pengecualian Ukur Failed.");

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
			
			sql = "insert into tblintanahppt (no_permohonan,tarikh_keputusan,catatan,flag_urusan,tarikh_terima,tarikh_masuk) " +
					"values ("+
					" '"+permohonan.getNoPU()+"'" +
					" ,to_date('"+permohonan.getTarikh()+"','dd/MM/yyyy')" +
					" ,'"+permohonan.getNoPU()+"'" +
					",'S',SYSDATE,SYSDATE) "+
					"";

			getPer().kemaskiniPermohonani(sql, stmt);

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
	
	private static ITanah getPer(){
		if(iPer==null){
			iPer = new PermohonanBean();
		}
		return iPer;
	
	}
	
	
}
