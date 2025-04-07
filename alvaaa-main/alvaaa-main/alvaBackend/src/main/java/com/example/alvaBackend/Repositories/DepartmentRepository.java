package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {

    Optional<Department> findByName(String name);
}
