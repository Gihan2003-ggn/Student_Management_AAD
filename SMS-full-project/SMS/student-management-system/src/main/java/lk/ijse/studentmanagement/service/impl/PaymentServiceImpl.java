package lk.ijse.studentmanagement.service.impl;

import lk.ijse.studentmanagement.dto.request.PaymentRequest;
import lk.ijse.studentmanagement.dto.response.PaymentResponse;
import lk.ijse.studentmanagement.entity.Payment;
import lk.ijse.studentmanagement.entity.PaymentStatus;
import lk.ijse.studentmanagement.entity.Student;
import lk.ijse.studentmanagement.exception.ResourceNotFoundException;
import lk.ijse.studentmanagement.repository.PaymentRepository;
import lk.ijse.studentmanagement.repository.StudentRepository;
import lk.ijse.studentmanagement.service.EmailService;
import lk.ijse.studentmanagement.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        if (paymentRepository.findByReceiptNumber(request.getReceiptNumber()).isPresent()) {
            throw new IllegalArgumentException("Receipt number already exists: " + request.getReceiptNumber());
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));

        Payment payment = Payment.builder()
                .receiptNumber(request.getReceiptNumber())
                .student(student)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.PAID)
                .paymentDate(LocalDate.now())
                .build();

        Payment saved = paymentRepository.save(payment);
        log.info("Payment created: {}", saved.getReceiptNumber());

        if (student.getUser() != null && student.getUser().getEmail() != null) {
            String studentName = student.getUser().getFirstName() + " " + student.getUser().getLastName();
            try {
                emailService.sendPaymentReceipt(
                        student.getUser().getEmail(),
                        studentName,
                        saved.getReceiptNumber(),
                        saved.getAmount(),
                        saved.getPaymentMethod());
            } catch (Exception e) {
                log.warn("Failed to send payment receipt email: {}", e.getMessage());
            }
        }

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStudent(UUID studentId) {
        return paymentRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse updatePaymentStatus(UUID id, String statusStr) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));

        try {
            PaymentStatus status = PaymentStatus.valueOf(statusStr.toUpperCase());
            payment.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment status. Must be PENDING, PAID, OVERDUE, or CANCELLED");
        }

        Payment updated = paymentRepository.save(payment);
        log.info("Payment status updated: {}", updated.getReceiptNumber());
        return mapToResponse(updated);
    }

    @Override
    public void deletePayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        paymentRepository.delete(payment);
        log.info("Payment deleted: {}", id);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .receiptNumber(payment.getReceiptNumber())
                .studentId(payment.getStudent() != null ? payment.getStudent().getId() : null)
                .studentIdCode(payment.getStudent() != null ? payment.getStudent().getStudentId() : null)
                .amount(payment.getAmount())
                .status(payment.getStatus() != null ? payment.getStatus().name() : null)
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
