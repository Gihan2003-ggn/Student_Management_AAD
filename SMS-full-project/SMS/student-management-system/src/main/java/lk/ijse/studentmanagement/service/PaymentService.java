package lk.ijse.studentmanagement.service;

import lk.ijse.studentmanagement.dto.request.PaymentRequest;
import lk.ijse.studentmanagement.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentById(UUID id);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByStudent(UUID studentId);

    PaymentResponse updatePaymentStatus(UUID id, String status);

    void deletePayment(UUID id);
}
