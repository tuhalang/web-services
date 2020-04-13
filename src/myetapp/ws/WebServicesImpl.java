package myetapp.ws;

import java.util.ResourceBundle;

import javax.jws.WebService;

import myetapp.entities.gis.RequestObjectGIS;
import myetapp.entities.gis.Tanah;
import myetapp.entities.mt.Document;
import myetapp.entities.mt.Holder;
import myetapp.entities.mt.Kaveat;
import myetapp.entities.mt.Registration;
import myetapp.integrasi.etanah.Permohonan;
import myetapp.integrasi.etanah.TanahApplicationResponse;
import myetapp.integrasi.etanah.TanahMelakaManager;
import myetapp.integrasi.etanah.kl.EtanahApplicationResponse;
import myetapp.integrasi.etanah.kl.EtanahManager;
import myetapp.integrasi.etanah.kl.ListData;
import myetapp.integrasi.gis.GISManager;
import myetapp.integrasi.insolvensi.InsApplicationResponse;
import myetapp.integrasi.insolvensi.InsManager;
import myetapp.integrasi.mt.MTManager;
import myetapp.integrasi.mt.MTPendaftaran;
import myetapp.integrasi.mt.RequestObject;
import myetapp.utils.IntLogManager;

//Service Implementation
@WebService(serviceName = "myeTappService", portName = "updateApplication", endpointInterface = "myetapp.ws.WebServices")
public class WebServicesImpl implements WebServices {
	static ResourceBundle rb = ResourceBundle.getBundle("ws");
	String user = rb.getString("USERNAME");
	String pwd = rb.getString("PASSWORD");
	UpdateApplicationResponse result_;
	
	@Override
	public UpdateApplicationResponse updateRegistration(String refID
		,String username
		,String password
		,String caseNo
		,String registrationDate
		,String remarks
		,String roomNo
		,String transactionID) {
		result_ = new UpdateApplicationResponse();
		
		if (username.equals(user) && password.equals(pwd)) {
			if (transactionID.isEmpty()) {
				result_ = new UpdateApplicationResponse();
				result_.setCode("1");
				result_.setDescription("Invalid parameter");
				result_.setDetail("Transaction ID is required.");
				
			} else {
				Registration requestObj = new Registration();
				requestObj.setRefID(refID);
				requestObj.setUsername(username);
				requestObj.setPassword(password);
				requestObj.setCaseNo(caseNo);
				requestObj.setRegistrationDate(registrationDate);
				requestObj.setRemarks(remarks);
				requestObj.setRoom(roomNo);
				requestObj.setTransactionID(transactionID);
				result_ = MTPendaftaran.updateRegistrationCase(requestObj);
				
			}
		
		} else {
			result_ = new UpdateApplicationResponse();
			result_.setCode("1");
			result_.setDescription("Invalid Credentials");
			result_.setDetail("Invalid Credentials");
		
		}		
		
		String flagSuccess = "";
		if (result_.getCode().equals("0")) {
			flagSuccess = "Y";
		} else {
			flagSuccess = "T";
		}
		
		IntLogManager.recordLogMT(refID, "C", "I", flagSuccess, result_.getDetail());
		
		return result_;
	
	}
	
	@Override
	public UpdateApplicationResponse updateApplicationRequest(
		String transactionID, String username, String password,
		String petitionNo, String formType, String formCreatedDate,
		String blueCardID, String remarks, Kaveat kaveat, Holder holder,
		Document document) {
		UpdateApplicationResponse result;

		formCreatedDate = formCreatedDate.replace('T', ' ');

		result = new UpdateApplicationResponse();
		if (username.equals(user) && password.equals(pwd)) {
			if (petitionNo.isEmpty()) {
				result = new UpdateApplicationResponse();
				result.setCode("1");
				result.setDescription("Invalid parameter");
				result.setDetail("petitionNo is required.");
				
			} else if (blueCardID.isEmpty()) {
				result = new UpdateApplicationResponse();
				result.setCode("1");
				result.setDescription("Invalid parameter");
				result.setDetail("blueCardID is required.");
				
			} else if ((!formType.equals("")) && (!formType.equals("WH"))
					&& (!formType.equals("YL"))) {
				result = new UpdateApplicationResponse();
				result.setCode("1");
				result.setDescription("Invalid parameter");
				result.setDetail("formType can only accept WH or YL.");
				
			} else {
				RequestObject requestObj = new RequestObject();
				requestObj.setTransactionID(transactionID);
				requestObj.setUsername(username);
				requestObj.setPassword(password);
				requestObj.setPetitionNo(petitionNo);
				requestObj.setFormType(formType);
				requestObj.setFormCreatedDate(formCreatedDate);
				requestObj.setBlueCardID(blueCardID);
				requestObj.setRemarks(remarks);
				result = MTManager.updateCFormResult(requestObj, kaveat,holder, document);
				
			}
		
		} else {
			result = new UpdateApplicationResponse();
			result.setCode("1");
			result.setDescription("Invalid Credentials");
			result.setDetail("Invalid Credentials");
		
		}		
		
		String flagSuccess = "";
		if (result.getCode().equals("0")) {
			flagSuccess = "Y";
		} else {
			flagSuccess = "T";
		}
		
		IntLogManager.recordLogMT(petitionNo, "C", "I", flagSuccess, result.getDetail());
		
		return result;
	
	}

	@Override
	public UpdateApplicationResponse appRequestGISUpdate(String transactionID,
			String username, String password, String fileNo, String upiCode,
			String latitude, String longLatitude, String createdUser,
			String createdDate, String statusTanah, String remarks) {
		UpdateApplicationResponse result;

		result = new UpdateApplicationResponse();
		if (username.equals(user) && password.equals(pwd)) {
			if (upiCode.isEmpty()) {
				result = new UpdateApplicationResponse();
				result.setCode("1");
				result.setDescription("Invalid parameter");
				result.setDetail("UPI Code is required.");

			} else if (createdUser.isEmpty()) {
				result = new UpdateApplicationResponse();
				result.setCode("1");
				result.setDescription("Invalid parameter");
				result.setDetail("User ID is required.");

			} else {
				RequestObjectGIS requestObj = new RequestObjectGIS();
				requestObj.setTransactionID(transactionID);
				requestObj.setUsername(username);
				requestObj.setPassword(password);
				requestObj.setNoFail(fileNo);
				requestObj.setUPICode(upiCode);
				requestObj.setLatitude(latitude);
				requestObj.setLongLatitude(longLatitude);
				requestObj.setCreatedDate(createdDate);
				requestObj.setCreatedID(createdUser);
				requestObj.setStatus(statusTanah);
				requestObj.setRemarks(remarks);
				result = GISManager.updateResult(requestObj);
	
			}
		
		} else {
			result = new UpdateApplicationResponse();
			result.setCode("1");
			result.setDescription("Invalid Credentials");
			result.setDetail("");
		
		}
		return result;
	
	}

	@Override
	public Tanah[] appRequestGISListing(String transactionID, String username,
		String password, String createdDate, String remarks) {
		Tanah[] result_ = new Tanah[1];
		Tanah result = null;
		if (username.equals(user) && password.equals(pwd)) {
			RequestObjectGIS requestObj = new RequestObjectGIS();
			requestObj.setTransactionID(transactionID);
			result_ = GISManager.getListing(requestObj);

		} else {
			result = new Tanah();
			result.setCode("1");
			result.setDescription("Invalid Credentials");
			result.setDetail("");
			result_[0] = result;

		}
		return result_;
	
	}

	@Override
	public Tanah appRequestGISInfo(String transactionID, String username,
			String password, String fileNo, String upiCode, String statusTanah,
			String remarks) {
		Tanah result;

		result = new Tanah();
		if (username.equals(user) && password.equals(pwd)) {
			if (fileNo.isEmpty()) {
				result = new Tanah();
				result.setCode("1");
				result.setDescription("Invalid parameter");
				result.setDetail("UPI Code is required.");
	
			} else {
				RequestObjectGIS requestObj = new RequestObjectGIS();
				requestObj.setTransactionID(transactionID);
				requestObj.setUsername(username);
				requestObj.setPassword(password);
				requestObj.setNoFail(fileNo);
				requestObj.setUPICode(upiCode);
				requestObj.setStatus(statusTanah);
				requestObj.setRemarks(remarks);
				result = GISManager.updateResultInfo(requestObj);
			
			}
		
		} else {
			result = new Tanah();
			result.setCode("1");
			result.setDescription("Invalid Credentials");
			result.setDetail("");
		
		}
		return result;
	
	}

	@Override
	public InsApplicationResponse getListMaklumatPusaka(String username,
		String password, String noKpSimati) {
		InsApplicationResponse result;

		result = new InsApplicationResponse();
		if (username.equals(user) && password.equals(pwd)) {
			result = InsManager.getListMaklumatPusaka(noKpSimati);
		} else {
			result = new InsApplicationResponse();
			result.setCode("1");
			result.setDescription("Invalid Credentials");
			result.setDetail("");
		
		}
		return result;
	
	}

	@Override
	public EtanahApplicationResponse etUpdateEndorsanPPT(String username,
		String password, String transactionID, String noFail, String noJilid,
		ListData listData) {
		EtanahApplicationResponse result;

		result = new EtanahApplicationResponse();
		if (username.equals(user) && password.equals(pwd)) {
			result = EtanahManager.etUpdateEndorsanPPT(transactionID, noFail, noJilid, listData);
		} else {
			result = new EtanahApplicationResponse();
			result.setCode("1");
			result.setDescription("Invalid Credentials");
			result.setDetail("");
		
		}
		return result;
	
	}
	/**
	 * 2020/04/13
	 * */
	@Override
	public TanahApplicationResponse eTanahPPTUpdateEndorsan(String username
		,String password, String idPermohonan, Permohonan permohonan) {
		TanahApplicationResponse result;

		result = new TanahApplicationResponse();
		if (username.equals(user) && password.equals(pwd)) {
			result = TanahMelakaManager.UpdateEndorsan(idPermohonan,permohonan);
		} else {
			result = new TanahApplicationResponse();
			result.setCode("1");
			result.setDescription("Invalid Credentials");
			result.setDetail("");
		
		}
		return result;
	
	}
	

}
