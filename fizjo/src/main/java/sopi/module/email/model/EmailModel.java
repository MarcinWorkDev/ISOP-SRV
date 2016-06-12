package sopi.module.email.model;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

@Component
public class EmailModel {

	@Autowired JavaMailSender mailSender;
	@Autowired private SpringTemplateEngine templateEngine;
	
	public void sendEmail(String to, String subject, String template, Map<String, Object> variables, boolean withLogo) {
		Context ctx = new Context();
		ctx.setVariables(variables);
		String emailText = templateEngine.process(template, ctx);

		this.sendMessage(to, subject, emailText, withLogo);
	}
	
	public void sendEmail(String to, String subject, String body) {
		this.sendMessage(to, subject, body, false);
	}
	
	private void sendMessage(String to, String subject, String text, boolean withLogo) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true, "UTF-8");
			
			InternetAddress from = new InternetAddress();
			from.setAddress("isop@marcin.work");
			from.setPersonal("ISOP - Internetowy System Obsługi Pacjentów");
			
			helper.setFrom(from);
			helper.setText(text, true);
			helper.setSubject(subject);
			helper.setTo(to);
			
			mailSender.send(message);
		} catch (MessagingException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

