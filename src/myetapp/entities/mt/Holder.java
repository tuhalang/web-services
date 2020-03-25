package myetapp.entities.mt;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Holder")
public class Holder {

	public String petitionNo;
	public String caseFilingDate;
	public String applicantName;
	public String petitioner;

	@XmlTransient
	public String getPetitionNo() {
		return petitionNo;
	}

	public void setPetitionNo(String petitionNo) {
		this.petitionNo = petitionNo;
	}

	@XmlTransient
	public String getCaseFilingDate() {
		return caseFilingDate;
	}

	public void setCaseFilingDate(String caseFilingDate) {
		this.caseFilingDate = caseFilingDate;
	}

	@XmlTransient
	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	@XmlTransient
	public String getPetitioner() {
		return petitioner;
	}

	public void setPetitioner(String petitioner) {
		this.petitioner = petitioner;
	}
}
