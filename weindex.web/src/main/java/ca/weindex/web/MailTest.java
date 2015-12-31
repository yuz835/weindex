package ca.weindex.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTest {
	public static void main(String[] args) {
		String d = "04/22/2013 3:33 +0800";
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm Z");
		try {
			Date dd = format.parse(d);
			System.out.println(dd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main2(String[] args) {
		Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{3,19}");
		String a = "1sdjoisdjf1";
		System.out.println(a + "--" + p.matcher(a).matches());
		a = "lk3";
		System.out.println(a + "--" + p.matcher(a).matches());
		a = "lk kdf";
		System.out.println(a + "--" + p.matcher(a).matches());
		a = "lksjflksjflkajfkldsjflkasjgerg09u5voe";
		System.out.println(a + "--" + p.matcher(a).matches());

		a = "lks3@";
		System.out.println(a + "--" + p.matcher(a).matches());

	}
	public static void mainq(String[] args) throws AddressException, MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("weindex.ca", "password@ames");
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("admin@weindex.ca"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("qizhang@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler," + "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
