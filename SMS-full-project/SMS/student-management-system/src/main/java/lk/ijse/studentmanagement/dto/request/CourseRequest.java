package lk.ijse.studentmanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseRequest {

    @NotBlank(message = "Course code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    private String code;

    @NotBlank(message = "Course title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    private String description;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    private Integer credits;

    @NotNull(message = "Department ID is required")
    private UUID departmentId;
}
