package lk.ijse.studentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentRequest {

    @NotBlank(message = "Department code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    private String code;

    @NotBlank(message = "Department name is required")
    @Size(max = 150, message = "Name cannot exceed 150 characters")
    private String name;

    private String description;
}
