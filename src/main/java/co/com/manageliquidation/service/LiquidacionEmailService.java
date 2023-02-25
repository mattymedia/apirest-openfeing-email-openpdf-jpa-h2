package co.com.manageliquidation.service;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class LiquidacionEmailService {
	private final String username = "developtestapi2023@gmail.com";
    private final String password = "sxvjchjozzgvczya";
    private final String recipient = "mjgarzon89@gmail.com";
    
    
   	public void sendEmail(Long idCard) throws IOException {
   		String URI_FILE = "pdf/user_" + idCard + ".pdf";
    	   //Configura las propiedades del servidor SMTP de gmail
   		Properties props = new Properties();
   		props.put("mail.smtp.auth", "true");
   		props.put("mail.smtp.starttls.enable", "true");
   		props.put("mail.smtp.host", "smtp.gmail.com");
   		props.put("mail.smtp.port", "587");

		// Crea una sesión de correo electrónico autenticada
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
        
        try {
            // Crea un mensaje de correo electrónico
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Correo de prueba desde Spring");
            
            BodyPart messageBodyPart = new MimeBodyPart(); 
            messageBodyPart.setText("Hola,\n\n Este es un correo de prueba enviado desde una aplicación java Spring.");
                        
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(URI_FILE));
            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);
           
            message.setContent(multipart);
            
            Transport.send(message);

            File archivo = new File(URI_FILE);
            archivo.delete();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
