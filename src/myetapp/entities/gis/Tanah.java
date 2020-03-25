package myetapp.entities.gis;

import java.io.Serializable;

import myetapp.ws.UpdateApplicationResponse;

public class Tanah extends UpdateApplicationResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 32837838744344849L;
	private String nofail;
	private String nohakmilik;
	private String jenishakmilik;
	private String statushakmilik;
	private String nowarta;
	private String kodlot;
	private String nolot;
	private String tarikhdaftar;
	private String kodnegeri;
	private String kodaerah;
	private String kodmukim;
	private String kodsek;
	private double luas;
	private String luasketerangan;
	private String kodagensi;
	private String kodkementerian;
	private String kegunaan;
	private String kodupi;
	private String status;
	//private String statusketerangan;
	private String latitude;
	private String longitude;
	private int idcharter;

	public String getNoFail() {
		return nofail;
	}
	public void setNoFail(String noFail) {
		this.nofail = noFail;
	}
	
	public String getNoHakmilik() {
		return nohakmilik;
	}
	public void setNoHakmilik(String noHakmilik) {
		this.nohakmilik = noHakmilik;
	}
	
	public String getJenisHakmilik() {
		return jenishakmilik;
	}
	public void setJenisHakmilik(String jenisHakmilik) {
		this.jenishakmilik = jenisHakmilik;
	}
	
	public String getStatusHakmilik() {
		return statushakmilik;
	}
	public void setStatusHakmilik(String statusHakmilik) {
		this.statushakmilik = statusHakmilik;
	}
	
	public String getNoWarta() {
		return nowarta;
	}
	public void setNoWarta(String noWarta) {
		this.nowarta = noWarta;
	}	
	
	public String getNoLot() {
		return nolot;
	}
	public void setNoLot(String noLot) {
		this.nolot = noLot;
	}
	
	public String getKodLot() {
		return kodlot;
	}
	public void setKodLot(String kodLot) {
		this.kodlot = kodLot;
	}
	
	public String getTarikhDaftar() {
		return tarikhdaftar;
	}
	public void setTarikhDaftar(String tarikhDaftar) {
		this.tarikhdaftar= tarikhDaftar;
	}	
	
	public String getKodNegeri() {
		return kodnegeri;
	}
	public void setKodNegeri(String kodNegeri) {
		this.kodnegeri = kodNegeri;
	}
	
	public String getKodDaerah() {
		return kodaerah;
	}
	public void setKodDaerah(String kodDaerah) {
		this.kodaerah = kodDaerah;
	}
	
	public String getKodMukim() {
		return kodmukim;
	}
	public void setKodMukim(String kodMukim) {
		this.kodmukim = kodMukim;
	}
	
	public String getKodSeksen() {
		return kodsek;
	}
	public void setKodSeksyen(String kodSek) {
		this.kodsek = kodSek;
	}	
	
	public double getLuas() {
		return luas;
	}
	public void setLuas(double luas) {
		this.luas = luas;
	}

	public String getKodAgensi() {
		return kodagensi;
	}
	public void setKodAgensi(String kodAgensi) {
		this.kodagensi =kodAgensi;
	}
	
	public String getKodKementerian() {
		return kodkementerian;
	}
	public void setKodKementerian(String kodKementerian) {
		this.kodkementerian = kodKementerian;
	}
	
	public String getKegunaan() {
		return kegunaan;
	}
	public void setKegunaan(String kegunaan) {
		this.kegunaan = kegunaan;
	}

	public String getUPI() {
		return kodupi;
	}
	public void setUPI(String kodUPI) {
		this.kodupi= kodUPI;
	}	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	
	public String getLuasKeterangan() {
		return luasketerangan;
	}
	public void setLuasKeterangan(String luasketerangan) {
		this.luasketerangan = luasketerangan;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLangitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public Integer getCharter() {
		return idcharter;
	}
	public void setCharter(int idcharter) {
		this.idcharter = idcharter;
	}
	
	
}
