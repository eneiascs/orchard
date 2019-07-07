package com.eneiascs.orchard.utility;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.eneiascs.orchard.model.AppUser;

@Component
public class EmailConstructor {
	@Autowired
	private Environment env;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	public MimeMessagePreparator constructNewUserEmail(AppUser user, String password) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("password", password);
		String text = templateEngine.process("newUserEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage)throws Exception{
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setPriority(1);
				email.setTo(user.getEmail());
				email.setSubject("Welcome to Orchard");
				email.setText(text, true);
				email.setFrom(new InternetAddress(env.getProperty("support.email")));
			}
		};
		
		return messagePreparator;
	}

	public MimeMessagePreparator constructUpdateProfileEmail(AppUser user) {
		Context context = new Context();
		context.setVariable("user", user);
		String text = templateEngine.process("updateUserEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage)throws Exception{
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setPriority(1);
				email.setTo(user.getEmail());
				email.setSubject("User updated");
				email.setText(text, true);
				email.setFrom(new InternetAddress(env.getProperty("support.email")));
			}
		};
		
		return messagePreparator;
	}

	public MimeMessagePreparator constructResetPasswordEmail(AppUser user, String password) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("password", password);
		String text = templateEngine.process("resetPasswordEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage)throws Exception{
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setPriority(1);
				email.setTo(user.getEmail());
				email.setSubject("Welcome to Orchard");
				email.setText(text, true);
				email.setFrom(new InternetAddress(env.getProperty("support.email")));
			}
		};
		
		return messagePreparator;
	}
}
