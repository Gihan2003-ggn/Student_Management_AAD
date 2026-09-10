package lk.ijse.studentmanagement.repository;

import lk.ijse.studentmanagement.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, UUID> {
    List<Timetable> findByBatchId(UUID batchId);

    List<Timetable> findByLecturerId(UUID lecturerId);
}
