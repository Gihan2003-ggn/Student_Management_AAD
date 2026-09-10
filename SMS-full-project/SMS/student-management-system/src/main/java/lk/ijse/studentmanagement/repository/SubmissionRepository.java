package lk.ijse.studentmanagement.repository;

import lk.ijse.studentmanagement.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    List<Submission> findByStudentId(UUID studentId);

    List<Submission> findByAssessmentId(UUID assessmentId);

    Optional<Submission> findByStudentIdAndAssessmentId(UUID studentId, UUID assessmentId);
}
