package lk.ijse.studentmanagement.repository;

import lk.ijse.studentmanagement.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUserEmail(String userEmail);

    List<AuditLog> findByEntityName(String entityName);
}
