package lk.ijse.studentmanagement.service;

import lk.ijse.studentmanagement.dto.request.StudentRequest;
import lk.ijse.studentmanagement.dto.response.StudentResponse;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    StudentResponse createStudent(StudentRequest request);

    StudentResponse getStudentById(UUID id);

    List<StudentResponse> getAllStudents();

    List<StudentResponse> getStudentsByBatch(UUID batchId);

    StudentResponse updateStudent(UUID id, StudentRequest request);

    void deleteStudent(UUID id);
}
