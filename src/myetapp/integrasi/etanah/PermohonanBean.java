package myetapp.integrasi.etanah;

import java.sql.Statement;

//import org.apache.log4j.Logger;

public class PermohonanBean implements ITanah {
	//private static Logger myLog = Logger.getLogger(PermohonanBean.class);
	//private static SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
	//private Db db = null;
	
	public void kemaskiniPermohonani(String sql,Statement stmt) throws Exception{
		stmt.execute(sql);
	}
	
	public void setResult(TanahApplicationResponse result){
		result.setCode("1");
		result.setDescription("Failed.");
	
	}
	
	
}
