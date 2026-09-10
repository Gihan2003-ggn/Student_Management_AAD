package lk.ijse.studentmanagement.service.impl;

import lk.ijse.studentmanagement.dto.request.CourseRequest;
import lk.ijse.studentmanagement.dto.response.CourseResponse;
import lk.ijse.studentmanagement.entity.Course;
import lk.ijse.studentmanagement.entity.Department;
import lk.ijse.studentmanagement.exception.ResourceNotFoundException;
import lk.ijse.studentmanagement.repository.CourseRepository;
import lk.ijse.studentmanagement.repository.DepartmentRepository;
import lk.ijse.studentmanagement.service.CourseService;
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
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Course code already exists: " + request.getCode());
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        Course course = Course.builder()
                .code(request.getCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .credits(request.getCredits())
                .department(department)
                .build();

        Course saved = courseRepository.save(course);
        log.info("Course created: {}", saved.getCode());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return mapToResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByDepartment(UUID departmentId) {
        return courseRepository.findByDepartmentId(departmentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse updateCourse(UUID id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setDepartment(department);

        Course updated = courseRepository.save(course);
        log.info("Course updated: {}", updated.getCode());
        return mapToResponse(updated);
    }

    @Override
    public void deleteCourse(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        courseRepository.delete(course);
        log.info("Course deleted: {}", id);
    }

    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .code(course.getCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .credits(course.getCredits())
                .departmentId(course.getDepartment() != null ? course.getDepartment().getId() : null)
                .departmentName(course.getDepartment() != null ? course.getDepartment().getName() : null)
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
