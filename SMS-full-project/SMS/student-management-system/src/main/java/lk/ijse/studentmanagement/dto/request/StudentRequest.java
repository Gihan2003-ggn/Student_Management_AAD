package lk.ijse.studentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class StudentRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Batch ID is required")
    private UUID batchId;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    private String address;
    private String phone;
    private LocalDate dateOfBirth;
}
