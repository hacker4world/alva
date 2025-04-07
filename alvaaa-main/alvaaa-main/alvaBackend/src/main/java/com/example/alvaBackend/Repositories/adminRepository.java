package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface adminRepository extends JpaRepository<Admin, Integer> {

}
