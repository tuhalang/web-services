package myetapp.integrasi.insolvensi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Vector;

import myetapp.db.DbManager;
import myetapp.entities.insolvensi.MaklumatPusaka;

import org.apache.log4j.Logger;

public class InsManager {

	static Logger myLogger = Logger.getLogger("InsManager");	

	public static InsApplicationResponse getListMaklumatPusaka(
			String noKpSimati) {

		InsApplicationResponse result = new InsApplicationResponse();

		try {
			if (noKpSimati != null && noKpSimati.length() > 0) {					
				Vector listMaklumatPusaka = getListPusaka(noKpSimati);
				if (listMaklumatPusaka.size() > 0) {
					result.setCode("0");
					result.setDescription("Success");
					result.setDetail("Success");
					result.setListMaklumatPusaka(listMaklumatPusaka);
				} else {
					result.setCode("1");
					result.setDescription("No Record Found.");
					result.setDetail("No Record Found.");
				}
			} else {
				result.setCode("1");
				result.setDescription("Failed.");
				result.setDetail("No. KP Simati is null.");
			}
		} catch (Exception ex) {
			result.setCode("1");
			result.setDescription("Failed.");
			result.setDetail(ex.getMessage());
		}
		return result;
	}

	private static Vector getListPusaka(String noKpSimati) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Vector listPusaka = null;
		Connection con = null;
		
		try {
			listPusaka = new Vector();
			con = DbManager.getInstance().getConnection();
			Statement stmt = con.createStatement();
			String sql = "SELECT PFD.NO_FAIL AS NO_FAIL, SIMATI.NAMA_SIMATI AS NAMA_SIMATI, SIMATI.NO_KP_BARU AS NO_KP_BARU, SIMATI.NO_KP_LAIN AS NO_KP_LAIN,"
					+ " SIMATI.NO_KP_LAMA AS NO_KP_LAMA, SIMATI.TARIKH_MATI AS TARIKH_MATI, PEMOHON.NAMA_PEMOHON AS NAMA_PEMOHON,"
					+ " PEMOHON.NO_KP_BARU AS NOKP_PEMOHON, SAUDARA.KETERANGAN AS HUBUNGAN, PEMOHON.ALAMAT_1 AS ALAMAT1_PEMOHON,"
					+ " PEMOHON.ALAMAT_2 AS ALAMAT2_PEMOHON, PEMOHON.ALAMAT_3 AS ALAMAT3_PEMOHON, PEMOHON.POSKOD AS POSKOD,"
					+ " BDR.KETERANGAN AS BANDAR, PEMOHON.ID_NEGERI AS ID_NEGERI, PPK.TARIKH_MOHON AS TARIKH_MOHON, STATUS.KETERANGAN AS STATUS"
					+ " FROM TBLPFDFAIL PFD, TBLPPKPERMOHONAN PPK, TBLPPKPERMOHONANSIMATI PSIMATI, TBLPPKSIMATI SIMATI, TBLPPKPEMOHON PEMOHON,"
					+ " TBLPPKRUJSAUDARA SAUDARA, TBLRUJSTATUS STATUS, TBLRUJBANDAR BDR"
					+ " WHERE PFD.ID_FAIL = PPK.ID_FAIL AND PPK.ID_PERMOHONAN = PSIMATI.ID_PERMOHONAN AND PPK.ID_PEMOHON = PEMOHON.ID_PEMOHON"
					+ " AND PSIMATI.ID_SIMATI = SIMATI.ID_SIMATI AND PPK.ID_STATUS = STATUS.ID_STATUS AND PEMOHON.ID_SAUDARA = SAUDARA.ID_SAUDARA"
					+ " AND PEMOHON.ID_BANDAR = BDR.ID_BANDAR(+) AND PPK.ID_STATUS NOT IN ('999', '169', '50', '47', '70', '152')";
			if (noKpSimati != null && noKpSimati.length() > 0) {
				sql = sql + " AND SIMATI.NO_KP_BARU = '" + noKpSimati + "'";
			}					
			sql = sql + " ORDER BY SIMATI.NAMA_SIMATI, PPK.TARIKH_MOHON";			
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				MaklumatPusaka pusaka = new MaklumatPusaka();
				
				pusaka.setNoFail(rs.getString("NO_FAIL"));
				pusaka.setNamaSimati(rs.getString("NAMA_SIMATI"));
				pusaka.setNoKpBaru(rs.getString("NO_KP_BARU"));
				pusaka.setNoKpLain(rs.getString("NO_KP_LAIN"));
				pusaka.setNoKpLama(rs.getString("NO_KP_LAMA"));
				pusaka.setTarikhMati(sdf.format(rs.getDate("TARIKH_MATI")));
				pusaka.setNamaPemohon(rs.getString("NAMA_PEMOHON"));
				pusaka.setNokpPemohon(rs.getString("NOKP_PEMOHON"));
				pusaka.setHubungan(rs.getString("HUBUNGAN"));
				pusaka.setAlamat1Pemohon(rs.getString("ALAMAT1_PEMOHON"));
				pusaka.setAlamat2Pemohon(rs.getString("ALAMAT2_PEMOHON"));
				pusaka.setAlamat3Pemohon(rs.getString("ALAMAT3_PEMOHON"));
				pusaka.setPoskod(rs.getString("POSKOD"));
				pusaka.setBandar(rs.getString("BANDAR"));
				pusaka.setIdNegeri(rs.getString("ID_NEGERI"));
				pusaka.setTarikhMohon(sdf.format(rs.getDate("TARIKH_MOHON")));
				pusaka.setStatus(rs.getString("STATUS"));
				listPusaka.add(pusaka);				
			}
		} finally {
			if (con != null)
				con.close();
		}
		
		return listPusaka;
	}
}
