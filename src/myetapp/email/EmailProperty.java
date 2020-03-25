package myetapp.email;

import java.util.ResourceBundle;

public class EmailProperty {
	private String host;
	private String port;
	private String from;
	private String pass;
	private final String BASENAME = "mail";
	private ResourceBundle rb = null;
	private volatile static EmailProperty singleton = null;

	private String mailFrom;
	private String mailTo;
	private String mailCc;

	private EmailProperty() {
		rb = ResourceBundle.getBundle(BASENAME);
	}

	public String getHost() {
		host = rb.getString("SMTP_HOST");
		return host;
	}

	public String getPort() {
		port = rb.getString("SMTP_PORT");
		return port;
	}

	public String getFrom() {
		from = rb.getString("SMTP_FROM");
		return from;
	}

	public String getPass() {
		pass = rb.getString("SMTP_PASS");
		return pass;
	}

	public String getMailFrom() {
		mailFrom = rb.getString("ADMIN_FROM");
		return mailFrom;
	}

	public String getMailTo() {
		mailTo = rb.getString("ADMIN_TO");
		return mailTo;
	}

	public String getMailCc() {
		mailCc = rb.getString("ADMIN_CC");
		return mailCc;
	}

	public static EmailProperty getInstance() {
		if (singleton == null) {
			synchronized (EmailProperty.class) {
				if (singleton == null) {
					singleton = new EmailProperty();
				}
			}
		}
		return singleton;
	}

	public static void main(String args[]) {
		System.out.println(EmailProperty.getInstance().getMailFrom());
	}

}
