package com.reachout.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Jessica Morgan
 */

public class EmailHandler {

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;


	public static void main(String args[]) throws AddressException, MessagingException, IOException {

		generateAndSendEmail("jesswales1993@yahoo.co.uk", "testuser", "./src/main/resources/signupEmail.html", "Welcome to ReachOut...");
		System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
	}


	public static void generateAndSendEmail(String email, String username, String filename, String emailSubject) throws AddressException, MessagingException{

		StringBuilder sb = new StringBuilder();
		
		BufferedReader rd = null;
		try {
			// Open the file for reading.
			rd = new BufferedReader(new FileReader(new File(filename)));

			// Read all contents of the file.
			String inputLine = null;
			while((inputLine = rd.readLine()) != null)
				sb.append((inputLine));
		}
		catch(IOException ex) {
			System.err.println("An IOException was caught!");
			ex.printStackTrace();
		}
		finally {
			// Close the file.
			try {
				rd.close();
			}
			catch (IOException ex) {
				System.err.println("An IOException was caught!");
				ex.printStackTrace();
			}
		}

		// Step1
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");

		// Step2
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		generateMailMessage.setSubject(emailSubject);

		generateMailMessage.setContent(sb.toString(), "text/html");

		System.out.println("Mail Session has been created successfully..");

		// Step3
		System.out.println("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		transport.connect("smtp.gmail.com", "reachoutapplication20@gmail.com", "Reachout2020");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
































}