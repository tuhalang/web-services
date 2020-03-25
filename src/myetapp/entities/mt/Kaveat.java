package myetapp.entities.mt;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Kaveat")
public class Kaveat {

	public String petitionNo;
	public String appliedDate;
	public String applicantName;
	public String representative;

	@XmlTransient
	public String getPetitionNo() {
		return petitionNo;
	}

	public void setPetitionNo(String petitionNo) {
		this.petitionNo = petitionNo;
	}

	@XmlTransient
	public String getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(String appliedDate) {
		this.appliedDate = appliedDate;
	}

	@XmlTransient
	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	@XmlTransient
	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}
}
