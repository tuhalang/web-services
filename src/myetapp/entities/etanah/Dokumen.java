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
