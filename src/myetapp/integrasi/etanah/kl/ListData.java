package myetapp.integrasi.etanah.kl;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import myetapp.entities.etanah.Dokumen;
import myetapp.entities.etanah.Hakmilik;

@XmlRootElement(name = "ListData")
public class ListData {
	public List<Hakmilik> listHakmilik;
	public List<Dokumen> listDokumen;

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
}
