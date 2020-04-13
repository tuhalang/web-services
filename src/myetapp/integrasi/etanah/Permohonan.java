package myetapp.integrasi.etanah;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import myetapp.entities.etanah.Dokumen;
import myetapp.entities.etanah.Hakmilik;

@XmlRootElement(name = "Permohonan")
public class Permohonan {
	public List<Hakmilik> listHakmilik;
	public List<Dokumen> listDokumen;
	public String noJilid;
	public String tarikh;
	public String catatan;
	public String jenis;

	@XmlTransient
	public List<Hakmilik> getListHakmilik() {
		return listHakmilik;
	}

	public void setListHakmilik(List<Hakmilik> listHakmilik) {
		this.listHakmilik = listHakmilik;
	}

	@XmlTransient
	public List<Dokumen> getListDokumen() {
		return listDokumen;
	}

	public void setListDokumen(List<Dokumen> listDokumen) {
		this.listDokumen = listDokumen;
	}
	
	@XmlTransient
	public String getNoJilid() {
		return noJilid;
	}

	public void setNoJilid(String noJilid) {
		this.noJilid = noJilid;
	}
	
	@XmlTransient
	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}
	
	@XmlTransient
	public String getJenis() {
		return jenis;
	}

	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
	
	@XmlTransient
	public String getTarikh() {
		return tarikh;
	}

	public void setTarikh(String tarikh) {
		this.tarikh = tarikh;
	}

	
}
