package myetapp.entities.etanah;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Dokumen")
public class Dokumen {

	public String flagDokumen;
	public String namaDokumen;
	public String docContent;

	@XmlTransient
	public String getFlagDokumen() {
		return flagDokumen;
	}
	/**
	 * FLAG_DOKUMEN IS 
	 * '1=Borang D; 2=Warta Kerajaan Persekutuan; 
	 * 3=Surat Iringan Borang D; 4=Jadual Bearing Distance & Coordinate;
	 * 5=Pelan Pengambilan Digital; 6=Surat Maklum;
	 * 7=Borang K; 8=Surat Iringan Borang K;
	 * 9=Borang D; 10=Warta Borang D; 
	 * 11=Surat Iringan Batalkan Endorsan Borang D; 12=Warta Tarik Balik; 
	 * 13=Surat Iringan Tarik Balik;14=Borang B; 
	 * 15=Sijil Bebas Ukur;16=Surat Permohonan Ukur'
	 * @param flagDokumen
	 */
	public void setFlagDokumen(String flagDokumen) {
		this.flagDokumen = flagDokumen;
	}

	@XmlTransient
	public String getNamaDokumen() {
		return namaDokumen;
	}

	public void setNamaDokumen(String namaDokumen) {
		this.namaDokumen = namaDokumen;
	}

	@XmlTransient
	public String getDocContent() {
		return docContent;
	}

	public void setDocContent(String docContent) {
		this.docContent = docContent;
	}
}
