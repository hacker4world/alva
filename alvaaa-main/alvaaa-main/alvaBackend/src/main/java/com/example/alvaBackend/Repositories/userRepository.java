package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Department;
import com.example.alvaBackend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User, Integer> {



    public Optional<User> findByMatriculeNumber(String matriculeNumber);

    Optional<User> findByEmailOrMatriculeNumberOrCin(String email, String matriculeNumber,String cin);

   // public User findByEmail(String email);
    public Optional<User> findByEmail(String email);

    public List<User> findByStatus(String status);

    Optional<User> findByCode(String token);

    List<User> findByDepartment(Department department);
}
