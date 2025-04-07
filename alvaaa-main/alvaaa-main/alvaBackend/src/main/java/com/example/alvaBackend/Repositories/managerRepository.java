package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface managerRepository extends JpaRepository<Manager,Integer> {
}
