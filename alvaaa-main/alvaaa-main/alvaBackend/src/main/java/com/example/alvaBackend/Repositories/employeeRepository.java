package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface employeeRepository extends JpaRepository<Employee,Integer> {
}
