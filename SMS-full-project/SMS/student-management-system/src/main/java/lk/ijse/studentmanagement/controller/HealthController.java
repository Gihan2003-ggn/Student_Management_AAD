package lk.ijse.studentmanagement.controller;

import lk.ijse.studentmanagement.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkHealth() {
        log.info("Health check requested");

        Map<String, Object> healthData = new HashMap<>();
        healthData.put("application", "Student Management System");
        healthData.put("status", "UP");
        healthData.put("timestamp", LocalDateTime.now().toString());

        try {
            Connection connection = dataSource.getConnection();
            boolean valid = connection.isValid(2);
            healthData.put("database", valid ? "Connected" : "Unreachable");
            healthData.put("databaseProduct", connection.getMetaData().getDatabaseProductName());
            connection.close();
            log.info("Database ping successful");
        } catch (Exception e) {
            log.error("Database connection failed: {}", e.getMessage());
            healthData.put("database", "Failed");
        }

        return ResponseEntity.ok(ApiResponse.success("System is healthy", healthData));
    }
}
