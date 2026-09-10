package lk.ijse.studentmanagement.service;

import lk.ijse.studentmanagement.dto.request.CourseRequest;
import lk.ijse.studentmanagement.dto.response.CourseResponse;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    CourseResponse createCourse(CourseRequest request);

    CourseResponse getCourseById(UUID id);

    List<CourseResponse> getAllCourses();

    List<CourseResponse> getCoursesByDepartment(UUID departmentId);

    CourseResponse updateCourse(UUID id, CourseRequest request);

    void deleteCourse(UUID id);
}
