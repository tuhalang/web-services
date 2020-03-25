package myetapp.entities.etanah;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Hakmilik")
public class Hakmilik {

	public String idHakmilik;
	public String idHakmilikSambungan;
	public String kodNegeri;
	public String kodDaerah;
	public String kodMukim;
	public String kodHakmilik;
	public String noHakmilik;
	public String kodLot;
	public String noLot;
	public String noSeksyen;
	public String kodLuasSambungan;
	public String luasSambungan;
	public String noPerserahan;
	public String tarikhEndorsan;
	public String masaEndorsan;

	@XmlTransient
	public String getIdHakmilik() {
		return idHakmilik;
	}

	public void setIdHakmilik(String idHakmilik) {
		this.idHakmilik = idHakmilik;
	}

	@XmlTransient
	public String getIdHakmilikSambungan() {
		return idHakmilikSambungan;
	}

	public void setIdHakmilikSambungan(String idHakmilikSambungan) {
		this.idHakmilikSambungan = idHakmilikSambungan;
	}

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
	public String getKodHakmilik() {
		return kodHakmilik;
	}

	public void setKodHakmilik(String kodHakmilik) {
		this.kodHakmilik = kodHakmilik;
	}

	@XmlTransient
	public String getNoHakmilik() {
		return noHakmilik;
	}

	public void setNoHakmilik(String noHakmilik) {
		this.noHakmilik = noHakmilik;
	}

	@XmlTransient
	public String getKodLot() {
		return kodLot;
	}

	public void setKodLot(String kodLot) {
		this.kodLot = kodLot;
	}

	@XmlTransient
	public String getNoLot() {
		return noLot;
	}

	public void setNoLot(String noLot) {
		this.noLot = noLot;
	}

	@XmlTransient
	public String getNoSeksyen() {
		return noSeksyen;
	}

	public void setNoSeksyen(String noSeksyen) {
		this.noSeksyen = noSeksyen;
	}

	@XmlTransient
	public String getKodLuasSambungan() {
		return kodLuasSambungan;
	}

	public void setKodLuasSambungan(String kodLuasSambungan) {
		this.kodLuasSambungan = kodLuasSambungan;
	}

	@XmlTransient
	public String getLuasSambungan() {
		return luasSambungan;
	}

	public void setLuasSambungan(String luasSambungan) {
		this.luasSambungan = luasSambungan;
	}

	@XmlTransient
	public String getNoPerserahan() {
		return noPerserahan;
	}

	public void setNoPerserahan(String noPerserahan) {
		this.noPerserahan = noPerserahan;
	}

	@XmlTransient
	public String getTarikhEndorsan() {
		return tarikhEndorsan;
	}

	public void setTarikhEndorsan(String tarikhEndorsan) {
		this.tarikhEndorsan = tarikhEndorsan;
	}

	@XmlTransient
	public String getMasaEndorsan() {
		return masaEndorsan;
	}

	public void setMasaEndorsan(String masaEndorsan) {
		this.masaEndorsan = masaEndorsan;
	}
}
