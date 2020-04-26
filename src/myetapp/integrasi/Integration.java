package myetapp.integrasi;

import myetapp.integrasi.etanah.Permohonan;
import myetapp.integrasi.etanah.TanahApplicationResponse;

public interface Integration {
	public boolean kemaskiniPermohonan(String idPermohonan,String transactionID,Permohonan permohonan) throws Exception;
	public TanahApplicationResponse semakanPermohonan(String idPermohonan,String transactionID,  Permohonan permohonan) throws Exception;

}
