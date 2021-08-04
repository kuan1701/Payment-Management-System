package com.webproject.pms.util.MailSender;

import com.webproject.pms.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Service
public class MailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;
    
    @Autowired
    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        
        mailSender.send(mailMessage);
    }
    
    public void sendVerificationEmail(User user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
    
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String verifyURL = siteURL + "/verify?code=" + user.getActivationCode();
        
        String toAddress = user.getEmail();
        String fromAddress = "paymentmanagementsystem2021@gmail.com";
        String senderName = "Payment Management System Support";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"" + verifyURL + "\">VERIFY</a></h3>"
                + "Thank you, [[name]].<br>"
                + "Payment Management System.";
    
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
    
        content = content.replace("[[name]]", user.getName());
        //content = content.replace("[[URL]]", verifyURL);
    
        helper.setText(content, true);
    
        mailSender.send(message);
    }
    
    public void sendEmailForResetPassword(String recipientEmail, String link) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setFrom("paymentmanagementsystem2021@gmail.com", "Payment Management System Support");
        helper.setTo(recipientEmail);
        
        String subject = "Here's the link to reset your password";
        
        String content = "<p>Hello!</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password.</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
    
    public static String getSiteURL(HttpServletRequest request) {
        String siteURl = request.getRequestURL().toString();
        return siteURl.replace(request.getServletPath(), "");
    }
}
