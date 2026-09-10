package lk.ijse.studentmanagement.repository;

import lk.ijse.studentmanagement.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findByStudentId(UUID studentId);

    List<Attendance> findByCourseIdAndDate(UUID courseId, LocalDate date);
}
