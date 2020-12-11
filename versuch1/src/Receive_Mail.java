import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.*;

public class Receive_Mail {
	public static void main(String[] args) throws Exception {
		fetchMail();
	}
	
	public static void fetchMail() {
		try {
			// your code here
			Properties props = new Properties();
			props.put("mail.pop3.host", "localhost");
			props.put("mail.pop3.port", "110");
			//props.put("mail.debug", "true");
			//props.put("mail.debug.quote", "true");
			Session session = Session.getDefaultInstance(props, null);
			//session.setDebug(true);

			Store store = session.getStore("pop3");
			store.connect("localhost", "labrat", "kn1lab");

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			Message[] messages = folder.getMessages();
			System.out.println(messages.length);
			for (int i = 0, n = messages.length; i < n; i++){
				Message message = messages[i];
				System.out.println("Nr:" + (i+1));
				System.out.println("Subject:" + message.getSubject());
				System.out.println("From:" + message.getFrom()[0]);
				System.out.println("Datum:" + message.getSentDate());
				System.out.println("Text:" + message.getContent().toString());
			}

			folder.close();
			store.close();


		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
