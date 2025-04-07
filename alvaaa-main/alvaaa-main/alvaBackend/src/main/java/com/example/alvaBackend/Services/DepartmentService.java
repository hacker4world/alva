package com.example.alvaBackend.Services;


import com.example.alvaBackend.Dto.DepartmentDto;
import com.example.alvaBackend.Dto.PostDto;
import com.example.alvaBackend.Dto.UpdateDepartmentDto;
import com.example.alvaBackend.Dto.UpdatePostDto;
import com.example.alvaBackend.Entities.Department;
import com.example.alvaBackend.Entities.Post;
import com.example.alvaBackend.Entities.User;
import com.example.alvaBackend.Repositories.DepartmentRepository;
import com.example.alvaBackend.Repositories.userRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final userRepository userRepository;

    public DepartmentService(DepartmentRepository departmentRepository,userRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> addDepartment(DepartmentDto department) {
        Optional<Department> optionalDepartment = departmentRepository.findByName(department.getName());
        if (optionalDepartment.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Department already exist!!");
        }

        Department newDepartment = new Department(
                department.getName()
        );

        departmentRepository.save(newDepartment);
        return ResponseEntity.ok("Department created Successfully!!");
    }

    public ResponseEntity<String> updateDepartment(int id, UpdateDepartmentDto updateDepartment) {
        try {
            Optional<Department> department = departmentRepository.findById(id);
            if (department.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Department with id " + id + " not found!");
            } else {
                Department existingDepartment = department.get();
                existingDepartment.setName(updateDepartment.getName());

                departmentRepository.save(existingDepartment);
                return ResponseEntity.status(200).body("Department updated successfully!");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<String> deleteDepartment(int id) {
        try {
            Optional<Department> optionalDepartment = departmentRepository.findById(id);
            if (optionalDepartment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Department with id " + id + " not found!");
            }
            Department department = optionalDepartment.get();
            List<User> users = userRepository.findByDepartment(department);
            if (!users.isEmpty()) {
                for (User user : users) {
                    user.setDepartment(null);
                    userRepository.save(user);
                }
            }
            departmentRepository.deleteById(id);
            return ResponseEntity.status(200).body("Department deleted successfully!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public  ResponseEntity<?> getDepartmentById(int id) {
        try {
            Optional<Department> department = departmentRepository.findById(id);
            if (department.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Department with id " + id + " not found!");
            } else {
                return ResponseEntity.ok(department.get());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<?> getAllDepartments() {
        try {
            List<Department> departments = departmentRepository.findAll();
            if (departments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No departments found!");
            } else {
                return ResponseEntity.ok(departments);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
