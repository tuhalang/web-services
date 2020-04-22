package myetapp.integrasi;

import myetapp.integrasi.etanah.Permohonan;

public interface Integration {
	public boolean kemaskiniPermohonan(String idPermohonan,String transactionID,Permohonan permohonan) throws Exception;

}
