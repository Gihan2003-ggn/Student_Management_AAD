package lk.ijse.studentmanagement.repository;

import lk.ijse.studentmanagement.entity.PaymentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentItemRepository extends JpaRepository<PaymentItem, UUID> {
    List<PaymentItem> findByPaymentId(UUID paymentId);
}
