package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository  extends JpaRepository<Reaction,Integer> {

}
