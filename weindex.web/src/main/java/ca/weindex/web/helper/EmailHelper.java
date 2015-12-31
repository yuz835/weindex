package ca.weindex.web.helper;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class EmailHelper {
	Properties props;
	
	public EmailHelper() {
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
	}
	
	private boolean sendMail(String receipt, String subject, String content) {
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("weindex.ca", "TODO");
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("admin@weindex.ca"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receipt));
			message.setSubject(subject);
			message.setText(content);
			
			Transport.send(message);

			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean sendLostPwdMail(String userName, String email, String token) {
		String subject = "[WeIndex.ca] 重置密码";
		String link = "http://weindex.ca/user/resetPwd.html?name=" + userName + "&token=" + token;
		String content = "WeIndex.ca\n\n如果您要重置用户 " + userName + " 的密码, 请访问以下链接: \n\n" + link + "\n\n谢谢!";
		return sendMail(email, subject, content);
	}

	public boolean sendLoginSuccessMail(String userName, String email) {
		String subject = "欢迎来到经商知道[WeIndex.ca]!";
		String content = "尊敬的 " + userName + ", \n\n恭喜您, 您已经在 经商知道WeIndex.ca 上注册成功! 经商知道是一个加拿大华人经商平台，你不仅可以在这发布商品，文章，分类信息，你还能绑定微博，在地图上定位，以及定时将你的商品，文章发送到微博。经商知道坚定相信社交网络将给你的生意带来质的飞跃!\n\nWeIndex.ca 管理团队";
		return sendMail(email, subject, content);
	}
	
}
