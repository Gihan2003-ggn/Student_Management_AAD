package lk.ijse.studentmanagement.repository;

import lk.ijse.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByStudentId(String studentId);

    Optional<Student> findByUserId(UUID userId);

    boolean existsByStudentId(String studentId);

    List<Student> findByBatchId(UUID batchId);
}
