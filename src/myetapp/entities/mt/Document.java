package myetapp.entities.mt;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Document")
public class Document {

	public String docID;
	public String docType;
	public String docContent;

	@XmlTransient
	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	@XmlTransient
	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;		
	}

	@XmlTransient
	public String getDocContent() {
		return docContent;	
	}

	public void setDocContent(String docContent) {
		this.docContent = docContent;
		System.out.println("lenght"+docContent.length());
	}
	
	
}
