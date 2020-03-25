package myetapp.entities.gis;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import myetapp.ws.UpdateApplicationResponse;

public class Permohonan extends UpdateApplicationResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5243757892616350479L;
	private String idPermohonan;
	private String noFail_;
	//private Tanah_bak hakmilik;
	private String kodNegeri;
	private String kodDaerah;
	private String kodMukim;
	private String kodSek;
	private double luas;
	private String kodAgensi;
	private String kodKementerian;
	private String tarikhPohon;
	private String kegunaan;
	private String status_;
	
	public String getIdPermohonan() {
		return idPermohonan;
	}
	public void setIdPermohonan(String idPermohonan) {
		this.idPermohonan = idPermohonan;
	}
	
	@XmlTransient
	public String getNoFail_() {
		return noFail_;
	}
	public void setNoFai_(String noFail) {
		this.noFail_ = noFail;
	}

//	public Tanah_bak getTanah() {
//		return hakmilik;
//	}
//	public void setTanah(Tanah_bak hakmilik) {
//		this.hakmilik = hakmilik;
//	}
	
	@XmlTransient
	public String getKodNegeri() {
		return kodNegeri;
	}
	public void setKodNegeri(String kodNegeri) {
		this.kodNegeri = kodNegeri;
	}
	
	@XmlTransient
	public String getKodDaerah() {
		return kodDaerah;
	}
	public void setKodDaerah(String kodDaerah) {
		this.kodDaerah = kodDaerah;
	}
	
	@XmlTransient
	public String getKodMukim() {
		return kodMukim;
	}
	public void setKodMukim(String kodMukim) {
		this.kodMukim = kodMukim;
	}
	
	@XmlTransient
	public String getKodSeksen() {
		return kodSek;
	}
	public void setKodSeksyen(String kodSek) {
		this.kodSek = kodSek;
	}	
	
	@XmlTransient
	public double getLuas() {
		return luas;
	}
	public void setLuas(double luas) {
		this.luas = luas;
	}
	
	@XmlTransient
	public String getKodAgensi() {
		return kodAgensi;
	}
	public void setKodAgensi(String kodAgensi) {
		this.kodAgensi =kodAgensi;
	}
	
	@XmlTransient
	public String getKodKementerian() {
		return kodKementerian;
	}
	public void setKodKementerian(String kodKementerian) {
		this.kodKementerian = kodKementerian;
	}

	@XmlTransient
	public String getTarikhPohon() {
		return tarikhPohon;
	}
	public void setTarikhPohon(String tarikhPohon) {
		this.tarikhPohon= tarikhPohon;
	}	
	
	@XmlTransient
	public String getKegunaan() {
		return kegunaan;
	}
	public void setKegunaan(String kegunaan) {
		this.kegunaan = kegunaan;
	}

	@XmlTransient
	public String getStatus_() {
		return status_;
	}
	public void setStatus_(String status) {
		this.status_ = status;
	}	
	
}
