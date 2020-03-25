package myetapp.ws;

import java.util.ResourceBundle;

import javax.xml.ws.Endpoint;

public class WebServicesPublisher {

	static ResourceBundle rb = null;

	public static void main(String[] args) {
		rb = ResourceBundle.getBundle("ws");
		String protocoll = rb.getString("PROL");
		String ip = rb.getString("IP_ADDRESS");
		String port = rb.getString("PORT");
		String path = rb.getString("PATH");
//		String ws_url = "https://" + ip + ":" + port + "/" + path;
		String ws_url = protocoll+"://" + ip + ":" + port + "/" + path;
		
		if (port.equals("80")) {
			ws_url = "http://" + ip + "/" + path;
		}
		System.out.println("Trying to start service at...[" + ws_url + "]");
		Endpoint.publish(ws_url, new WebServicesImpl());
		System.out.println("MyeTaPP Web Services is running...[" + ws_url + "]");
		
	}
	
	
}