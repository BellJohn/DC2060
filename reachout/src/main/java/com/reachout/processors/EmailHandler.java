package com.reachout.processors;

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

	public static void main(String args[]) throws AddressException, MessagingException {
		generateAndSendEmail("jesswales1993@yahoo.co.uk", "testuser");
		System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
	}

	public static void generateAndSendEmail(String email, String username) throws AddressException, MessagingException {

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
		generateMailMessage.setSubject("Welcome to Reachout..");

				
		String emailBody2 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
				"<html xmlns=\"https://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"><head>\r\n" + 
				"<!--[if gte mso 9]><xml>\r\n" + 
				"<o:OfficeDocumentSettings>\r\n" + 
				"<o:AllowPNG/>\r\n" + 
				"<o:PixelsPerInch>96</o:PixelsPerInch>\r\n" + 
				"</o:OfficeDocumentSettings>\r\n" + 
				"</xml><![endif]-->\r\n" + 
				"<title>Christmas Email template</title>\r\n" + 
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n" + 
				"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + 
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0 \">\r\n" + 
				"<meta name=\"format-detection\" content=\"telephone=no\">\r\n" + 
				"<!--[if !mso]><!-->\r\n" + 
				"<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700,800\" rel=\"stylesheet\">\r\n" + 
				"<!--<![endif]-->\r\n" + 
				"<style type=\"text/css\">\r\n" + 
				"body {\r\n" + 
				"	margin: 0 !important;\r\n" + 
				"	padding: 0 !important;\r\n" + 
				"	-webkit-text-size-adjust: 100% !important;\r\n" + 
				"	-ms-text-size-adjust: 100% !important;\r\n" + 
				"	-webkit-font-smoothing: antialiased !important;\r\n" + 
				"}\r\n" + 
				"img {\r\n" + 
				"	border: 0 !important;\r\n" + 
				"	outline: none !important;\r\n" + 
				"}\r\n" + 
				"p {\r\n" + 
				"	Margin: 0px !important;\r\n" + 
				"	Padding: 0px !important;\r\n" + 
				"}\r\n" + 
				"table {\r\n" + 
				"	border-collapse: collapse;\r\n" + 
				"	mso-table-lspace: 0px;\r\n" + 
				"	mso-table-rspace: 0px;\r\n" + 
				"}\r\n" + 
				"td, a, span {\r\n" + 
				"	border-collapse: collapse;\r\n" + 
				"	mso-line-height-rule: exactly;\r\n" + 
				"}\r\n" + 
				".ExternalClass * {\r\n" + 
				"	line-height: 100%;\r\n" + 
				"}\r\n" + 
				".em_defaultlink a {\r\n" + 
				"	color: inherit !important;\r\n" + 
				"	text-decoration: none !important;\r\n" + 
				"}\r\n" + 
				"span.MsoHyperlink {\r\n" + 
				"	mso-style-priority: 99;\r\n" + 
				"	color: inherit;\r\n" + 
				"}\r\n" + 
				"span.MsoHyperlinkFollowed {\r\n" + 
				"	mso-style-priority: 99;\r\n" + 
				"	color: inherit;\r\n" + 
				"}\r\n" + 
				" @media only screen and (min-width:481px) and (max-width:699px) {\r\n" + 
				".em_main_table {\r\n" + 
				"	width: 100% !important;\r\n" + 
				"}\r\n" + 
				".em_wrapper {\r\n" + 
				"	width: 100% !important;\r\n" + 
				"}\r\n" + 
				".em_hide {\r\n" + 
				"	display: none !important;\r\n" + 
				"}\r\n" + 
				".em_img {\r\n" + 
				"	width: 100% !important;\r\n" + 
				"	height: auto !important;\r\n" + 
				"}\r\n" + 
				".em_h20 {\r\n" + 
				"	height: 20px !important;\r\n" + 
				"}\r\n" + 
				".em_padd {\r\n" + 
				"	padding: 20px 10px !important;\r\n" + 
				"}\r\n" + 
				"}\r\n" + 
				"@media screen and (max-width: 480px) {\r\n" + 
				".em_main_table {\r\n" + 
				"	width: 100% !important;\r\n" + 
				"}\r\n" + 
				".em_wrapper {\r\n" + 
				"	width: 100% !important;\r\n" + 
				"}\r\n" + 
				".em_hide {\r\n" + 
				"	display: none !important;\r\n" + 
				"}\r\n" + 
				".em_img {\r\n" + 
				"	width: 100% !important;\r\n" + 
				"	height: auto !important;\r\n" + 
				"}\r\n" + 
				".em_h20 {\r\n" + 
				"	height: 20px !important;\r\n" + 
				"}\r\n" + 
				".em_padd {\r\n" + 
				"	padding: 20px 10px !important;\r\n" + 
				"}\r\n" + 
				".em_text1 {\r\n" + 
				"	font-size: 16px !important;\r\n" + 
				"	line-height: 24px !important;\r\n" + 
				"}\r\n" + 
				"u + .em_body .em_full_wrap {\r\n" + 
				"	width: 100% !important;\r\n" + 
				"	width: 100vw !important;\r\n" + 
				"}\r\n" + 
				"}\r\n" + 
				"</style>\r\n" + 
				"</head>\r\n" + 
				" \r\n" + 
				"<body class=\"em_body\" style=\"margin:0px; padding:0px;\" bgcolor=\"#efefef\">\r\n" + 
				"<table class=\"em_full_wrap\" valign=\"top\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" bgcolor=\"#efefef\" align=\"center\">\r\n" + 
				"  <tbody><tr>\r\n" + 
				"    <td valign=\"top\" align=\"center\"><table class=\"em_main_table\" style=\"width:700px;\" width=\"700\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\">\r\n" + 
				"        <!--Header section-->\r\n" + 
				"        <tbody><tr>\r\n" + 
				"          <td style=\"padding:15px;\" class=\"em_padd\" valign=\"top\" bgcolor=\"#f6f7f8\" align=\"center\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\">\r\n" + 
				"              <tbody><tr>\r\n" + 
				"                <td style=\"font-family:'Open Sans', Arial, sans-serif; font-size:12px; line-height:15px; color:#0d1121;\" valign=\"top\" align=\"center\">ReachOut signup email</td>\r\n" + 
				"              </tr>\r\n" + 
				"            </tbody></table></td>\r\n" + 
				"        </tr>\r\n" + 
				"        <!--//Header section--> \r\n" + 
				"        <!--Banner section-->\r\n" + 
				"        <tr>\r\n" + 
				"          <td valign=\"top\" align=\"center\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\">\r\n" + 
				"              <tbody><tr>\r\n" + 
				"                <td valign=\"top\" align=\"center\"><img class=\"em_img\" alt=\"ReachOut logo\" style=\"display:block; font-family:Arial, sans-serif; font-size:30px; line-height:34px; color:#000000; max-width:700px;\" src=\"https://cs2410-web02pvm.aston.ac.uk/~morganjc/images/reachout.png\" width=\"700\" border=\"0\" height=\"345\"></td>\r\n" + 
				"              </tr>\r\n" + 
				"            </tbody></table></td>\r\n" + 
				"        </tr>\r\n" + 
				"        <!--//Banner section--> \r\n" + 
				"        <!--Content Text Section-->\r\n" + 
				"                 <tr>\r\n" + 
				"          <td style=\"padding:35px 70px 30px;\" class=\"em_padd\" valign=\"top\" bgcolor=\"#19aa8d\" align=\"center\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\">\r\n" + 
				"              <tbody><tr>\r\n" + 
				"                <td style=\"font-family:'Open Sans', Arial, sans-serif; font-size:16px; line-height:30px; color:#ffffff;\" valign=\"top\" align=\"center\">Welcome to ReachOut, your username is <b>" + username + "</b></td>\r\n" + 
				"              </tr>\r\n" + 
				"              <tr>\r\n" + 
				"                <td style=\"font-size:0px; line-height:0px; height:15px;\" height=\"15\">&nbsp;</td>\r\n" + 
				"<!--—this is space of 15px to separate two paragraphs ---->\r\n" + 
				"              </tr>\r\n" + 
				"              <tr>\r\n" + 
				"                <td style=\"font-family:'Open Sans', Arial, sans-serif; font-size:18px; line-height:22px; color:#fbeb59; letter-spacing:2px; padding-bottom:12px;\" valign=\"top\" align=\"center\">Thank you for signing up to ReachOut, there is just one more step before you can start making/accepting requests.</td>\r\n" + 
				"              </tr>\r\n" + 
				"              <tr>\r\n" + 
				"                <td class=\"em_h20\" style=\"font-size:0px; line-height:0px; height:25px;\" height=\"25\">&nbsp;</td>\r\n" + 
				"<!--—this is space of 25px to separate two paragraphs ---->\r\n" + 
				"              </tr>\r\n" + 
				"<tr>\r\n" + 
				"                <td style=\"font-family:'Open Sans', Arial, sans-serif; font-size:18px; line-height:22px; color:#fbeb59; text-transform:uppercase; letter-spacing:2px; padding-bottom:12px;\" valign=\"top\" align=\"center\"> Please click <b><a href = \"http://localhost:8080/ReachOut/validated\"> here</a> </b> to verify your account</td>\r\n" + 
				"              </tr>\r\n" + 
				"            </tbody></table></td>\r\n" + 
				"        </tr>\r\n" + 
				" \r\n" + 
				"        <!--//Content Text Section--> \r\n" + 
				"        <!--Footer Section-->\r\n" + 
				"        <tr>\r\n" + 
				"          <td style=\"padding:38px 30px;\" class=\"em_padd\" valign=\"top\" bgcolor=\"#f6f7f8\" align=\"center\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\">\r\n" + 
				"              <tbody><tr>\r\n" + 
				"                <td style=\"padding-bottom:16px;\" valign=\"top\" align=\"center\"><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\">\r\n" +              
				"                  </tbody></table></td>\r\n" + 
				"              </tr>\r\n" + 
				"              <tr>\r\n" + 
				"                <td style=\"font-family:'Open Sans', Arial, sans-serif; font-size:11px; line-height:18px; color:#999999;\" valign=\"top\" align=\"center\"><a href=\"#\" target=\"_blank\" style=\"color:#999999; text-decoration:underline;\">PRIVACY STATEMENT</a> | <a href=\"#\" target=\"_blank\" style=\"color:#999999; text-decoration:underline;\">TERMS OF SERVICE</a> | <a href=\"#\" target=\"_blank\" style=\"color:#999999; text-decoration:underline;\">RETURNS</a><br>\r\n" + 
				"                  ©2020 ReachOut.  All Rights Reserved.<br>\r\n" + 
				"                  If you do not wish to receive any further emails from us, please <a href=\"#\" target=\"_blank\" style=\"text-decoration:none; color:#999999;\">unsubscribe</a></td>\r\n" + 
				"              </tr>\r\n" + 
				"            </tbody></table></td>\r\n" + 
				"        </tr>\r\n" + 
				"        <tr>\r\n" + 
				"          <td class=\"em_hide\" style=\"line-height:1px;min-width:700px;background-color:#ffffff;\"><img alt=\"\" src=\"images/spacer.gif\" style=\"max-height:1px; min-height:1px; display:block; width:700px; min-width:700px;\" width=\"700\" border=\"0\" height=\"1\"></td>\r\n" + 
				"        </tr>\r\n" + 
				"      </tbody></table></td>\r\n" + 
				"  </tr>\r\n" + 
				"</tbody></table>\r\n" + 
				"<div class=\"em_hide\" style=\"white-space: nowrap; display: none; font-size:0px; line-height:0px;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div>\r\n" + 
				"</body></html>";
		
		generateMailMessage.setContent(emailBody2, "text/html");
		
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