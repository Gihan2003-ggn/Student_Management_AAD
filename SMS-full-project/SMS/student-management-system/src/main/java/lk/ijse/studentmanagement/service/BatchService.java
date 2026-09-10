package lk.ijse.studentmanagement.service;

import lk.ijse.studentmanagement.dto.request.BatchRequest;
import lk.ijse.studentmanagement.dto.response.BatchResponse;

import java.util.List;
import java.util.UUID;

public interface BatchService {

    BatchResponse createBatch(BatchRequest request);

    BatchResponse getBatchById(UUID id);

    List<BatchResponse> getAllBatches();

    List<BatchResponse> getBatchesByCourse(UUID courseId);

    BatchResponse updateBatch(UUID id, BatchRequest request);

    void deleteBatch(UUID id);
}
