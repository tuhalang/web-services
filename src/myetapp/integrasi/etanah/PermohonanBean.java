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

public class PermohonanBean implements ITanah {
	private static Logger myLog = Logger.getLogger(PermohonanBean.class);
	private static SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
	//private Db db = null;
	private Connection con = null;		
	private String sql = "";

	public void kemaskiniPermohonani(String sql,Statement stmt) throws Exception{
		stmt.execute(sql);
	}
	
	public void setResult(TanahApplicationResponse result){
		result.setCode("1");
		result.setDescription("Failed.");
	
	}
	
	
}
