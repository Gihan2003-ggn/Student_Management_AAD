package lk.ijse.studentmanagement.repository;

import lk.ijse.studentmanagement.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GradeRepository extends JpaRepository<Grade, UUID> {
    Optional<Grade> findBySubmissionId(UUID submissionId);
}
