package sopi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

	@Bean
	public JavaMailSenderImpl mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("mail.marcin.work");
		mailSender.setUsername("isop@marcin.work");
		mailSender.setPassword("brLuHuLyp");
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setPort(587);
		
		return mailSender;
	}
}
