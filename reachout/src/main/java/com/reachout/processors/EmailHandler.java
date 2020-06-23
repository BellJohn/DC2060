package com.reachout.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.reachout.blog.PostReader;

/**
 * @author Jessica Morgan
 */

public class EmailHandler {

	private static final Logger logger = LogManager.getLogger(EmailHandler.class);

	// Hidden empty constructor for static method provider
	private EmailHandler() {
	}

	public static void generateAndSendEmail(String email, String filename, String emailSubject) {
		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage generateMailMessage;
		// get actual path of file
		String emailFile = PostReader.getFilePath(filename);
		StringBuilder sb = new StringBuilder();

		try (BufferedReader rd = new BufferedReader(new FileReader(new File(emailFile)))) {
			// Open the file for reading.

			// Read all contents of the file.
			String inputLine = null;
			while ((inputLine = rd.readLine()) != null) {
				sb.append((inputLine));
			}
		} catch (IOException ex) {
			logger.error(ex);
		}
		Transport transport = null;
		try {
			// Step1
			logger.debug("Setup Mail Server Properties.");
			mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.port", "587");
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
			logger.debug("Mail Server Properties have been setup successfully.");

			// Step2
			logger.debug("Get Mail Session..");
			getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			generateMailMessage = new MimeMessage(getMailSession);
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			generateMailMessage.setSubject(emailSubject);

			generateMailMessage.setContent(sb.toString(), "text/html");

			logger.debug("Mail Session has been created successfully.");

			// Step3
			logger.debug("Get Session and Send mail");
			transport = getMailSession.getTransport("smtp");

			// Enter your correct gmail UserID and Password

			SystemPropertiesService sps = SystemPropertiesService.getInstance();
			transport.connect("smtp.gmail.com", sps.getProperty("EMAIL_ACC"), sps.getProperty("EMAIL_PASS"));
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			logger.debug("Sign up email sent to user");
		} catch (MessagingException e) {
			logger.error(e);
		} finally {
			try {
				if (transport != null) {
					transport.close();
				}
			} catch (MessagingException e) {
				logger.fatal("Unable to shut down resource for mail");
			}
		}
	}

	public static void generateAndSendPasswordResetEmail(String email, String filename, String emailSubject, String url)
			throws MessagingException {
		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage generateMailMessage;
		// get actual path of file
		String emailFile = PostReader.getFilePath(filename);
		StringBuilder sb = new StringBuilder();

		try (BufferedReader rd = new BufferedReader(new FileReader(new File(emailFile)))) {
			// Open the file for reading.

			// Read all contents of the file.
			String inputLine = null;
			while ((inputLine = rd.readLine()) != null) {
				if (inputLine.contains("PASSWORD_RESET_EMAIL_URL")) {
					inputLine = inputLine.replaceAll("PASSWORD_RESET_EMAIL_URL", url);
				}
				sb.append((inputLine));
			}
		} catch (IOException ex) {
			logger.error("Unable to build email", ex);
			return;
		}

		// Step1
		logger.debug("Setup Mail Server Properties Start");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		logger.debug("Mail Server Properties have been setup successfully");

		// Step2
		logger.debug("Get Mail Session Start");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		generateMailMessage.setSubject(emailSubject);

		generateMailMessage.setContent(sb.toString(), "text/html");

		logger.debug("Mail Session has been created successfully");

		// Step3
		logger.debug("Get Session and Send mail Start");
		Transport transport = getMailSession.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		SystemPropertiesService sps = SystemPropertiesService.getInstance();
		transport.connect("smtp.gmail.com", sps.getProperty("EMAIL_ACC"), sps.getProperty("EMAIL_PASS"));
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
		logger.debug("Get Session and Send mail Success");
	}

}
