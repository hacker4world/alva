package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface workerRepository extends JpaRepository<Worker,Integer> {

}
