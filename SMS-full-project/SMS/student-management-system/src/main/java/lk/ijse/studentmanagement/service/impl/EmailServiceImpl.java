package lk.ijse.studentmanagement.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.studentmanagement.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    @Async
    public void sendPaymentReceipt(String recipientEmail, String studentName, String receiptNumber, BigDecimal amount,
            String paymentMethod) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("Payment Receipt - " + receiptNumber + " | Student Management System");

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;'>"
                    + "<div style='background-color: #2563eb; color: #ffffff; padding: 20px; text-align: center;'>"
                    + "<h2>Payment Confirmation</h2>"
                    + "</div>"
                    + "<div style='padding: 24px; color: #333333;'>"
                    + "<p>Dear <strong>" + studentName + "</strong>,</p>"
                    + "<p>Thank you for your payment. Here is your official payment receipt details:</p>"
                    + "<table style='width: 100%; border-collapse: collapse; margin-top: 16px;'>"
                    + "<tr style='border-bottom: 1px solid #eeeeee;'><td style='padding: 10px; font-weight: bold;'>Receipt Number:</td><td style='padding: 10px;'>"
                    + receiptNumber + "</td></tr>"
                    + "<tr style='border-bottom: 1px solid #eeeeee;'><td style='padding: 10px; font-weight: bold;'>Amount Paid:</td><td style='padding: 10px; color: #16a34a; font-weight: bold;'>LKR "
                    + amount + "</td></tr>"
                    + "<tr style='border-bottom: 1px solid #eeeeee;'><td style='padding: 10px; font-weight: bold;'>Payment Method:</td><td style='padding: 10px;'>"
                    + (paymentMethod != null ? paymentMethod : "N/A") + "</td></tr>"
                    + "<tr style='border-bottom: 1px solid #eeeeee;'><td style='padding: 10px; font-weight: bold;'>Status:</td><td style='padding: 10px; color: #2563eb; font-weight: bold;'>PAID</td></tr>"
                    + "</table>"
                    + "<br/><p style='font-size: 13px; color: #666666;'>This is an automatically generated receipt. Please retain this email for your records.</p>"
                    + "</div>"
                    + "<div style='background-color: #f8fafc; padding: 12px; text-align: center; font-size: 12px; color: #94a3b8;'>"
                    + "&copy; 2026 Student Management System | ITS1114 Advanced API Development"
                    + "</div>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Payment receipt email sent asynchronously to {}", recipientEmail);
        } catch (MessagingException e) {
            log.error("Failed to send payment receipt email to {}: {}", recipientEmail, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendWelcomeEmail(String recipientEmail, String firstName, String role) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("Welcome to Student Management System!");

            String htmlContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;'>"
                    + "<div style='background-color: #10b981; color: #ffffff; padding: 20px; text-align: center;'>"
                    + "<h2>Welcome to Student Management System!</h2>"
                    + "</div>"
                    + "<div style='padding: 24px; color: #333333;'>"
                    + "<p>Hello <strong>" + firstName + "</strong>,</p>"
                    + "<p>Your account has been registered successfully on our platform.</p>"
                    + "<p><strong>Account Role:</strong> " + role + "</p>"
                    + "<p>You can now log in using your registered email address and access your dashboard services.</p>"
                    + "<br/><p style='font-size: 13px; color: #666666;'>If you did not create this account, please contact the system administration immediately.</p>"
                    + "</div>"
                    + "<div style='background-color: #f8fafc; padding: 12px; text-align: center; font-size: 12px; color: #94a3b8;'>"
                    + "&copy; 2026 Student Management System | ITS1114 Advanced API Development"
                    + "</div>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Welcome email sent asynchronously to {}", recipientEmail);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to {}: {}", recipientEmail, e.getMessage());
        }
    }
}

