package lk.ijse.studentmanagement.service.impl;

import lk.ijse.studentmanagement.dto.request.DepartmentRequest;
import lk.ijse.studentmanagement.dto.response.DepartmentResponse;
import lk.ijse.studentmanagement.entity.Department;
import lk.ijse.studentmanagement.exception.ResourceNotFoundException;
import lk.ijse.studentmanagement.repository.DepartmentRepository;
import lk.ijse.studentmanagement.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Department code already exists: " + request.getCode());
        }

        Department department = modelMapper.map(request, Department.class);
        Department saved = departmentRepository.save(department);
        log.info("Department created: {}", saved.getCode());
        return modelMapper.map(saved, DepartmentResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return modelMapper.map(department, DepartmentResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(d -> modelMapper.map(d, DepartmentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse updateDepartment(UUID id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        Department updated = departmentRepository.save(department);
        log.info("Department updated: {}", updated.getCode());
        return modelMapper.map(updated, DepartmentResponse.class);
    }

    @Override
    public void deleteDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        departmentRepository.delete(department);
        log.info("Department deleted: {}", id);
    }
}
