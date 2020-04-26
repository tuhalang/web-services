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
	public String noFail;
	public String noJilid;
	public String noPU;
	public String noRujukan;
	public String noWarta;
	public String tarikh;
	public String tarikhKeputusan;
	public String tarikhWarta;
//	public String tarikhSijil;
	public String catatan;
	public String catatanKeputusan;
	public String jenis;
	public String keputusan;

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
	/**
	 * A = MMK (SEK4); B = BORANG B/ WARTA;
	 * C = MMK (SEK*); D = BORANG D/WARTA;
	 * K = BORANG K; PD=PENARIKANBALIK BORANG D/WARTA;
	 * S = SIJIL PEMBEBASAN UKUR, PU = PERMOHONAN UKUR;
	 * @param jenis
	 */
	public void setJenis(String jenis) {
		this.jenis = jenis;
	}
	
	@XmlTransient
	public String geTarikh() {
		return tarikh;
	}

	public void seTarikh(String tarikh) {
		this.tarikh = tarikh;
	}
	
	@XmlTransient
	public String getKeputusan() {
		return keputusan;
	}

	public void setKeputusan(String keputusan) {
		this.keputusan = keputusan;
	}
	@XmlTransient
	public String getNoWarta() {
		return noWarta;
	}

	public void setNoWarta(String noWarta) {
		this.noWarta = noWarta;
	}

	@XmlTransient
	public String getNoJilid() {
		return noJilid;
	}

	public void setNoJilid(String noJilid) {
		this.noJilid = noJilid;
	}	

	@XmlTransient
	public String getNoPU() {
		return noPU;
	}

	public void setNoPU(String noPU) {
		this.noPU = noPU;
	}
	
	@XmlTransient
	public String getNoRujukan() {
		return noRujukan;
	}

	public void setNoRujukan(String noRujukan) {
		this.noFail = noRujukan;
	}
	
	@XmlTransient
	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}
	
	@XmlTransient
	public String geTarikhKeputusan() {
		return tarikhKeputusan;
	}

	public void seTarikhKeputusan(String tarikhKeputusan) {
		this.tarikhKeputusan = tarikhKeputusan;
	}
	
	@XmlTransient
	public String geTarikhWarta() {
		return tarikhWarta;
	}

	public void seTarikhWarta(String tarikhWarta) {
		this.tarikhWarta = tarikhWarta;
	}

	@XmlTransient
	public String getCatatanKeputusan() {
		return catatanKeputusan;
	}

	public void setCatatanKeputusan(String catatanKeputusan) {
		this.catatanKeputusan = catatanKeputusan;
	}
	
}
