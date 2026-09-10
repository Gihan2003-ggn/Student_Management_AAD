package lk.ijse.studentmanagement.service;

import java.math.BigDecimal;

public interface EmailService {

    void sendPaymentReceipt(String recipientEmail, String studentName, String receiptNumber, BigDecimal amount,
            String paymentMethod);

    void sendWelcomeEmail(String recipientEmail, String firstName, String role);
}

