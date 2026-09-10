package lk.ijse.studentmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {

    private UUID id;
    private String studentId;
    private UUID userId;
    private String userEmail;
    private String firstName;
    private String lastName;
    private UUID batchId;
    private String batchName;
    private LocalDate enrollmentDate;
    private String address;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
