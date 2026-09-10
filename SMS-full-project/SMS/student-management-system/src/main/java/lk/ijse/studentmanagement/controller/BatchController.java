package lk.ijse.studentmanagement.controller;

import jakarta.validation.Valid;
import lk.ijse.studentmanagement.dto.request.BatchRequest;
import lk.ijse.studentmanagement.dto.response.BatchResponse;
import lk.ijse.studentmanagement.service.BatchService;
import lk.ijse.studentmanagement.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BatchResponse>> createBatch(@Valid @RequestBody BatchRequest request) {
        BatchResponse response = batchService.createBatch(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Batch created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BatchResponse>> getBatchById(@PathVariable UUID id) {
        BatchResponse response = batchService.getBatchById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BatchResponse>>> getAllBatches(
            @RequestParam(required = false) UUID courseId) {
        List<BatchResponse> response = (courseId != null)
                ? batchService.getBatchesByCourse(courseId)
                : batchService.getAllBatches();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BatchResponse>> updateBatch(
            @PathVariable UUID id,
            @Valid @RequestBody BatchRequest request) {
        BatchResponse response = batchService.updateBatch(id, request);
        return ResponseEntity.ok(ApiResponse.success("Batch updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteBatch(@PathVariable UUID id) {
        batchService.deleteBatch(id);
        return ResponseEntity.ok(ApiResponse.success("Batch deleted successfully", null));
    }
}
