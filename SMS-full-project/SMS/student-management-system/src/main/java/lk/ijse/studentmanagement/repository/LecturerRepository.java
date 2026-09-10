package lk.ijse.studentmanagement.repository;

import lk.ijse.studentmanagement.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, UUID> {
    Optional<Lecturer> findByLecturerId(String lecturerId);

    Optional<Lecturer> findByUserId(UUID userId);

    boolean existsByLecturerId(String lecturerId);
}
