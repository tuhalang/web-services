package myetapp.integrasi.insolvensi;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import myetapp.entities.insolvensi.MaklumatPusaka;

@XmlRootElement(name = "InsApplicationResponse")
public class InsApplicationResponse {
	public String code;
	public String description;
	public String detail;
	public List<MaklumatPusaka> listMaklumatPusaka;

	@XmlTransient
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlTransient
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlTransient
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	@XmlTransient
	public List<MaklumatPusaka> getListMaklumatPusaka() {
		return listMaklumatPusaka;
	}

	public void setListMaklumatPusaka(List<MaklumatPusaka> listMaklumatPusaka) {
		this.listMaklumatPusaka = listMaklumatPusaka;
	}

}
