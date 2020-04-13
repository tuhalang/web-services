package myetapp.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

//import myetapp.entities.etanah.Dokumen;
//import myetapp.entities.etanah.Hakmilik;
import myetapp.entities.gis.Tanah;
import myetapp.entities.mt.Document;
import myetapp.entities.mt.Holder;
import myetapp.entities.mt.Kaveat;
import myetapp.integrasi.etanah.Permohonan;
import myetapp.integrasi.etanah.TanahApplicationResponse;
import myetapp.integrasi.etanah.kl.EtanahApplicationResponse;
import myetapp.integrasi.etanah.kl.ListData;
import myetapp.integrasi.insolvensi.InsApplicationResponse;

@WebService
@SOAPBinding(style = Style.RPC)
public interface WebServices {
	//Kemaskini No.Kes
	@WebMethod
	@WebResult(name = "UpdateApplicationResponse")
	UpdateApplicationResponse updateRegistration(@WebParam(name = "refID") String refID
			,@WebParam(name = "username") String username
			,@WebParam(name = "password") String password
			,@WebParam(name = "caseNo") String caseNo
			,@WebParam(name = "registrationDate") String registrationDate
			,@WebParam(name = "remarks") String remarks
			,@WebParam(name = "roomNo") String roomNo
			,@WebParam(name = "transactionID") String transactionID
			);

	@WebMethod
	@WebResult(name = "UpdateApplicationResponse")
	UpdateApplicationResponse updateApplicationRequest(@WebParam(name = "transactionID") String transactionID,
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "petitionNo") String petitionNo,
			@WebParam(name = "formType") String formType,
			@WebParam(name = "formCreatedDate") String formCreatedDate,
			@WebParam(name = "blueCardID") String blueCardID,
			@WebParam(name = "remarks") String remarks,
			@WebParam(name = "kaveat") Kaveat kaveat,
			@WebParam(name = "holder") Holder holder,
			@WebParam(name = "document") Document document);

	@WebMethod
	@WebResult(name = "UpdateApplicationResponse")
	UpdateApplicationResponse appRequestGISUpdate(@WebParam(name = "transactionID") String transactionID,
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "fileNo") String fileno,
			@WebParam(name = "upiCode") String upicode,
			@WebParam(name = "latitude") String latitude,
			@WebParam(name = "longLatitude") String longLatitude,
			@WebParam(name = "createdUser") String createdUser,
			@WebParam(name = "createdDate") String createdDate,
			@WebParam(name = "statusTanah") String statusTanah,
			@WebParam(name = "remarks") String remarks);

	@WebMethod
	@WebResult(name = "UpdateApplicationResponse")
	Tanah[] appRequestGISListing(@WebParam(name = "transactionID") String transactionID,
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "createdDate") String createdDate,
			@WebParam(name = "remarks") String remarks);

	@WebMethod
	@WebResult(name = "UpdateApplicationResponse")
	Tanah appRequestGISInfo(@WebParam(name = "transactionID") String transactionID,
			@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "fileNo") String fileno,
			@WebParam(name = "upiCode") String upicode,
			@WebParam(name = "statusTanah") String statusTanah,
			@WebParam(name = "remarks") String remarks);

	@WebMethod
	@WebResult(name = "InsApplicationResponse")
	InsApplicationResponse getListMaklumatPusaka(@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "noKpSimati") String noKpSimati);
	
	@WebMethod
	@WebResult(name = "EtanahApplicationResponse")
	EtanahApplicationResponse etUpdateEndorsanPPT(@WebParam(name = "username") String username,
			@WebParam(name = "password") String password,
			@WebParam(name = "transactionID") String transactionID,
			@WebParam(name = "noFail") String noFail,
			@WebParam(name = "noJilid") String noJilid,
			@WebParam(name = "listData") ListData listData);
	
	@WebMethod
	@WebResult(name = "TanahApplicationResponse")
	TanahApplicationResponse eTanahPPTUpdateEndorsan(@WebParam(name = "username") String username
			,@WebParam(name = "password") String password
			,@WebParam(name = "idPermohonan") String idPermohonan
			,@WebParam(name = "permohonan") Permohonan permohonan);


}
