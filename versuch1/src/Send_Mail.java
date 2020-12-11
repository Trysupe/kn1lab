import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.*;


public class Send_Mail {
	public static void main(String[] args) {
		sendMail();   
	}
	
	public static void sendMail() {
		try {
			// your code here
			Properties props = new Properties();
			props.put("mail.smtp.host", "localhost");
			Session session = Session.getInstance(props, null);

			Message msg = new MimeMessage(session);
			msg.setSubject("Neu");
			msg.setSentDate(new Date());
			msg.setFrom(new InternetAddress("sender@localhost"));
			msg.setRecipient(Message.RecipientType.TO,
					new InternetAddress("labrat@localhost"));
			msg.setText("ganz viel text");

			Transport.send(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
