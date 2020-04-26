package myetapp.integrasi.etanah;

import java.sql.Statement;

import myetapp.integrasi.etanah.TanahApplicationResponse;

public interface ITanah {
	public void kemaskiniPermohonani(String sql,Statement stmt) throws Exception;
	
	public void setResult(TanahApplicationResponse result);
	
}
