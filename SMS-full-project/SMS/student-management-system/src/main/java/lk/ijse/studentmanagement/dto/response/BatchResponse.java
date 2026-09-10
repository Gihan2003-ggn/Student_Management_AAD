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
public class BatchResponse {

    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int capacity;
    private UUID courseId;
    private String courseTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
