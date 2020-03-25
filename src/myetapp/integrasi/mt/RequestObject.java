package myetapp.integrasi.mt;

public class RequestObject {
	public String transactionID;
	public String username;
	public String password;
	public String petitionNo;
	public String formType;
	public String formCreatedDate;
	public String blueCardID;
	public String remarks;

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPetitionNo() {
		return petitionNo;
	}

	public void setPetitionNo(String petitionNo) {
		this.petitionNo = petitionNo;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getFormCreatedDate() {
		return formCreatedDate;
	}

	public void setFormCreatedDate(String formCreatedDate) {
		this.formCreatedDate = formCreatedDate;
	}

	public String getBlueCardID() {
		return blueCardID;
	}

	public void setBlueCardID(String blueCardID) {
		this.blueCardID = blueCardID;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
