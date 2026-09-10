package lk.ijse.studentmanagement.service.impl;

import lk.ijse.studentmanagement.dto.request.StudentRequest;
import lk.ijse.studentmanagement.dto.response.StudentResponse;
import lk.ijse.studentmanagement.entity.Batch;
import lk.ijse.studentmanagement.entity.Student;
import lk.ijse.studentmanagement.entity.User;
import lk.ijse.studentmanagement.exception.ResourceNotFoundException;
import lk.ijse.studentmanagement.repository.BatchRepository;
import lk.ijse.studentmanagement.repository.StudentRepository;
import lk.ijse.studentmanagement.repository.UserRepository;
import lk.ijse.studentmanagement.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private BatchRepository batchRepository;

        @Override
        public StudentResponse createStudent(StudentRequest request) {
                if (studentRepository.existsByStudentId(request.getStudentId())) {
                        throw new IllegalArgumentException("Student ID already exists: " + request.getStudentId());
                }

                User user = userRepository.findById(request.getUserId())
                                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

                Batch batch = batchRepository.findById(request.getBatchId())
                                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", request.getBatchId()));

                Student student = Student.builder()
                                .studentId(request.getStudentId())
                                .user(user)
                                .batch(batch)
                                .enrollmentDate(request.getEnrollmentDate())
                                .address(request.getAddress())
                                .phone(request.getPhone())
                                .dateOfBirth(request.getDateOfBirth())
                                .build();

                Student saved = studentRepository.save(student);
                log.info("Student created: {}", saved.getStudentId());
                return mapToResponse(saved);
        }

        @Override
        @Transactional(readOnly = true)
        public StudentResponse getStudentById(UUID id) {
                Student student = studentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
                return mapToResponse(student);
        }

        @Override
        @Transactional(readOnly = true)
        public List<StudentResponse> getAllStudents() {
                return studentRepository.findAll().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<StudentResponse> getStudentsByBatch(UUID batchId) {
                return studentRepository.findByBatchId(batchId).stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public StudentResponse updateStudent(UUID id, StudentRequest request) {
                Student student = studentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

                Batch batch = batchRepository.findById(request.getBatchId())
                                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", request.getBatchId()));

                student.setBatch(batch);
                student.setEnrollmentDate(request.getEnrollmentDate());
                student.setAddress(request.getAddress());
                student.setPhone(request.getPhone());
                student.setDateOfBirth(request.getDateOfBirth());

                Student updated = studentRepository.save(student);
                log.info("Student updated: {}", updated.getStudentId());
                return mapToResponse(updated);
        }

        @Override
        public void deleteStudent(UUID id) {
                Student student = studentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
                studentRepository.delete(student);
                log.info("Student deleted: {}", id);
        }

        private StudentResponse mapToResponse(Student student) {
                return StudentResponse.builder()
                                .id(student.getId())
                                .studentId(student.getStudentId())
                                .userId(student.getUser() != null ? student.getUser().getId() : null)
                                .userEmail(student.getUser() != null ? student.getUser().getEmail() : null)
                                .firstName(student.getUser() != null ? student.getUser().getFirstName() : null)
                                .lastName(student.getUser() != null ? student.getUser().getLastName() : null)
                                .batchId(student.getBatch() != null ? student.getBatch().getId() : null)
                                .batchName(student.getBatch() != null ? student.getBatch().getName() : null)
                                .enrollmentDate(student.getEnrollmentDate())
                                .address(student.getAddress())
                                .phone(student.getPhone())
                                .dateOfBirth(student.getDateOfBirth())
                                .createdAt(student.getCreatedAt())
                                .updatedAt(student.getUpdatedAt())
                                .build();
        }
}
