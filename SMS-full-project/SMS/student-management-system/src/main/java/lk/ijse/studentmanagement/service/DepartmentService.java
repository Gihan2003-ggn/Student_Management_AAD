package lk.ijse.studentmanagement.service;

import lk.ijse.studentmanagement.dto.request.DepartmentRequest;
import lk.ijse.studentmanagement.dto.response.DepartmentResponse;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);

    DepartmentResponse getDepartmentById(UUID id);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse updateDepartment(UUID id, DepartmentRequest request);

    void deleteDepartment(UUID id);
}
