package lk.ijse.studentmanagement.repository;

import lk.ijse.studentmanagement.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, UUID> {
    List<StudentCourse> findByStudentId(UUID studentId);

    List<StudentCourse> findByCourseId(UUID courseId);

    boolean existsByStudentIdAndCourseId(UUID studentId, UUID courseId);
}
