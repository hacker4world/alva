package com.example.alvaBackend.Controllers;

import com.example.alvaBackend.Dto.DepartmentDto;
import com.example.alvaBackend.Dto.UpdateDepartmentDto;
import com.example.alvaBackend.Entities.Post;
import com.example.alvaBackend.Services.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departmentManagement")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/addDepartment")
    public ResponseEntity<String> addDepartment(@RequestBody DepartmentDto department) {
        return departmentService.addDepartment(department);
    }

    @PutMapping("/updateDepartment/{id}")
    public ResponseEntity<String> updateDepartment(@PathVariable int id,@RequestBody UpdateDepartmentDto updatedDepartment) {
        return departmentService.updateDepartment(id,updatedDepartment);
    }


    @DeleteMapping("/deleteDepartment/{id}")
    public ResponseEntity<String>  deleteDepartment(@PathVariable int id) {
        return departmentService.deleteDepartment(id);
    }

    @GetMapping("/getDepartment/{id}")
    public ResponseEntity<?>  getDepartment(@PathVariable int id) {
        return departmentService.getDepartmentById(id);
    }

    @GetMapping("/getAllDepartments")
    public ResponseEntity<?>  getAllDepartments() {
        return departmentService.getAllDepartments();
    }
}
