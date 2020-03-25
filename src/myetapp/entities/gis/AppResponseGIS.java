package myetapp.entities.gis;

import javax.xml.bind.annotation.XmlTransient;

import myetapp.ws.UpdateApplicationResponse;



public class AppResponseGIS extends UpdateApplicationResponse {

	public String upiCode;
	public String noFail;
	public String status;

	@XmlTransient
	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	@XmlTransient
	public String getUPICode() {
		return upiCode;
	}

	public void setUPICode(String upiCode) {
		this.upiCode = upiCode;
	}

	@XmlTransient
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
