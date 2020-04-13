package myetapp.email;

import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSMTP {

	private static MailSMTP singleton;
	private static EmailProperty pro = EmailProperty.getInstance();
	private static final String EMAIL_HOST = pro.getHost();
	private static final int SMTP_PORT = Integer.parseInt(pro.getPort());
	private static final String FROM = pro.getFrom();
	private static final String PASS = pro.getPass();
	private Properties props = null;
	private Session mailSession = null;
	private MimeMessage msg = null;
	private InternetAddress multipleTo[];
	private InternetAddress multipleCC[];
	private File fileAttachments[];

	public String emailType = null;
	public String MESSAGE = null;
	public String RECIEPIENT = null;
	public String SUBJECT = null;
	public String MULTIPLE_RECIEPIENT[];
	public String TO_CC[];
	public String ATTACHMENT[];

	private MailSMTP() {
		getMailProperties();
		getMailSession();

	}

	public static synchronized MailSMTP getInstance() {
		singleton = new MailSMTP();
		return singleton;

	}

	private Properties getMailProperties() {
		this.emailType = ResourceBundle.getBundle("mail").getString("emel");
		props = new Properties();

		if ("gmail".equals(this.emailType)) {
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.host", EMAIL_HOST);
			props.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT));
		} else {
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", EMAIL_HOST);
			props.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT));
			props.setProperty("mail.smtp.ehlo", "false");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.localhost", "mail.hla-group.com");
		}

		return props;

	}

	private Session getMailSession() {
		if ("gmail".equals(this.emailType)) {
			mailSession = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(FROM, PASS);
						}
					});
		} else {
			mailSession = Session.getDefaultInstance(props);
		}

		return mailSession;

	}

	public void setEmail(String username, String password, String[] mailTo,
			String[] mailCc, String mailSubject, String mailText)
			throws MessagingException, Exception {

		SUBJECT = mailSubject;
		MESSAGE = mailText;
		MULTIPLE_RECIEPIENT = mailTo;

		TO_CC = mailCc;

		sendEmail();
	}

	/*
	 * public void setEmail(String username, String password, String mailTo,
	 * String mailCc, String mailSubject, String mailText) throws
	 * MessagingException {
	 * 
	 * SUBJECT = mailSubject; MESSAGE = mailText; MULTIPLE_RECIEPIENT = new
	 * String[] {mailTo}; if (mailCc != null) { TO_CC = mailCc; }
	 * 
	 * 
	 * sendEmail(); }
	 */

	public void setEmail(String username, String password, String mailTo,
			String mailCc, String mailSubject, String mailText)
			throws MessagingException, Exception {

		SUBJECT = mailSubject;
		MESSAGE = mailText;

		if (mailTo != "" && mailTo.contains(",")) {
			MULTIPLE_RECIEPIENT = mailTo.split(",");
		} else {
			MULTIPLE_RECIEPIENT = new String[] { mailTo };
		}

		if (mailCc != "" && mailCc.contains(",")) {
			TO_CC = mailCc.split(",");
		} else if (mailCc != "") {
			TO_CC = new String[] { mailCc };
		} else {
			TO_CC = new String[0];
		}

		sendEmail();
	}

	private MimeMessage setMessage() throws Exception {
		msg = new MimeMessage(mailSession);
		if (MESSAGE == null)
			throw new Exception("EMAIL DONT HAVE ANY MESSAGE");
		if (SUBJECT == null)
			throw new Exception("EMAIL DONT HAVE ANY SUBJECT");
		if (RECIEPIENT == null && MULTIPLE_RECIEPIENT.length <= 0)
			throw new Exception("EMAIL DONT HAVE ANY RECIEPIENT");

		if (MULTIPLE_RECIEPIENT != null && MULTIPLE_RECIEPIENT.length > 0) {
			multipleTo = new InternetAddress[MULTIPLE_RECIEPIENT.length];
			for (int i = 0; i < MULTIPLE_RECIEPIENT.length; i++) {
				String to = MULTIPLE_RECIEPIENT[i];
				if (to != null && !to.equals(""))
					multipleTo[i] = new InternetAddress(
							MULTIPLE_RECIEPIENT[i].trim());
			}
			// System.out.println("MULTIPLE EMAIL RECEIPIENT : " + multipleTo);
		}

		if (TO_CC != null && TO_CC.length > 0) {
			multipleCC = new InternetAddress[TO_CC.length];
			for (int i = 0; i < TO_CC.length; i++) {
				String cc = TO_CC[i];
				if (cc != null && !cc.equals("")) {
					multipleCC[i] = new InternetAddress(TO_CC[i].trim());
				}
			}
			// System.out.println("MULTIPLE EMAIL CC : " + multipleCC);
		}
		if (ATTACHMENT != null && ATTACHMENT.length > 0) {
			int totalAttachment = ATTACHMENT.length;

			fileAttachments = new File[totalAttachment];
			for (int i = 0; i < totalAttachment; i++) {
				fileAttachments[i] = new File(ATTACHMENT[i]);
				System.out.println(fileAttachments[i].getName());
			}
		}
		msg.setSubject(SUBJECT);
		Multipart mp = new MimeMultipart();

		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(MESSAGE, "text/html");
		if (fileAttachments != null && fileAttachments.length > 0) {
			for (int i = 0; i < fileAttachments.length; i++) {
				MimeBodyPart attachPart = new MimeBodyPart();
				FileDataSource source = new FileDataSource(fileAttachments[i]);
				attachPart.setDataHandler(new DataHandler(source));
				attachPart.setFileName(fileAttachments[i].getName());
				mp.addBodyPart(attachPart);
			}
		}
		mp.addBodyPart(textPart);

		msg.setContent(mp);
		msg.setFrom(new InternetAddress(FROM));

		if (multipleTo != null && multipleTo.length > 0) {
			msg.addRecipients(Message.RecipientType.TO, multipleTo);
		} else {
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					RECIEPIENT));
		}
		if (multipleCC != null && multipleCC.length > 0) {
			// CHECK AGAIN
			if (multipleCC[0] != null) {
				msg.addRecipients(Message.RecipientType.CC, multipleCC);
			}

		}
		return msg;

	}

	public void sendEmail() throws MessagingException, Exception {
		try {
			setMessage();
			Transport transport = mailSession.getTransport();
			transport.connect();
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();

		} catch (MessagingException x) {
			throw x;
		} catch (Exception e) {
			throw e;
		}

	}

	public static void main(String args[]) {
		// System.out.println(EmailProperty.getInstance().getMailFrom());
		// public void setEmail(String username, String password, String mailTo,
		// String mailCc, String mailSubject, String mailText)
		try {
			MailSMTP obj = new MailSMTP();
			//obj.setEmail("myetapp2017@gmail.com", "etapp2017",
				obj.setEmail("etapp.pla@gmail.com", "etapp123",
					"roslizakaria@gmail.com", "", "Email Test Subject",
					"Test Mail Content");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}