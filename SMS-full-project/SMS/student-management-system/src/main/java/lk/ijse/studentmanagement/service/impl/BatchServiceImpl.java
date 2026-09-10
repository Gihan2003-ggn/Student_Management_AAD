package lk.ijse.studentmanagement.service.impl;

import lk.ijse.studentmanagement.dto.request.BatchRequest;
import lk.ijse.studentmanagement.dto.response.BatchResponse;
import lk.ijse.studentmanagement.entity.Batch;
import lk.ijse.studentmanagement.entity.Course;
import lk.ijse.studentmanagement.exception.ResourceNotFoundException;
import lk.ijse.studentmanagement.repository.BatchRepository;
import lk.ijse.studentmanagement.repository.CourseRepository;
import lk.ijse.studentmanagement.service.BatchService;
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
public class BatchServiceImpl implements BatchService {

        @Autowired
        private BatchRepository batchRepository;

        @Autowired
        private CourseRepository courseRepository;

        @Override
        public BatchResponse createBatch(BatchRequest request) {
                Course course = courseRepository.findById(request.getCourseId())
                                .orElseThrow(() -> new ResourceNotFoundException("Course", "id",
                                                request.getCourseId()));

                Batch batch = Batch.builder()
                                .name(request.getName())
                                .startDate(request.getStartDate())
                                .endDate(request.getEndDate())
                                .capacity(request.getCapacity())
                                .course(course)
                                .build();

                Batch saved = batchRepository.save(batch);
                log.info("Batch created: {}", saved.getName());
                return mapToResponse(saved);
        }

        @Override
        @Transactional(readOnly = true)
        public BatchResponse getBatchById(UUID id) {
                Batch batch = batchRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id));
                return mapToResponse(batch);
        }

        @Override
        @Transactional(readOnly = true)
        public List<BatchResponse> getAllBatches() {
                return batchRepository.findAll().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<BatchResponse> getBatchesByCourse(UUID courseId) {
                return batchRepository.findByCourseId(courseId).stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public BatchResponse updateBatch(UUID id, BatchRequest request) {
                Batch batch = batchRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id));

                Course course = courseRepository.findById(request.getCourseId())
                                .orElseThrow(() -> new ResourceNotFoundException("Course", "id",
                                                request.getCourseId()));

                batch.setName(request.getName());
                batch.setStartDate(request.getStartDate());
                batch.setEndDate(request.getEndDate());
                batch.setCapacity(request.getCapacity());
                batch.setCourse(course);

                Batch updated = batchRepository.save(batch);
                log.info("Batch updated: {}", updated.getName());
                return mapToResponse(updated);
        }

        @Override
        public void deleteBatch(UUID id) {
                Batch batch = batchRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", id));
                batchRepository.delete(batch);
                log.info("Batch deleted: {}", id);
        }

        private BatchResponse mapToResponse(Batch batch) {
                return BatchResponse.builder()
                                .id(batch.getId())
                                .name(batch.getName())
                                .startDate(batch.getStartDate())
                                .endDate(batch.getEndDate())
                                .capacity(batch.getCapacity())
                                .courseId(batch.getCourse() != null ? batch.getCourse().getId() : null)
                                .courseTitle(batch.getCourse() != null ? batch.getCourse().getTitle() : null)
                                .createdAt(batch.getCreatedAt())
                                .updatedAt(batch.getUpdatedAt())
                                .build();
        }
}
