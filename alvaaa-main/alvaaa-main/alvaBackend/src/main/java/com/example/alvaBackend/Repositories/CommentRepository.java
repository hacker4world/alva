package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
    List<Comment> findCommentsByUserId(@Param("userId") int userId);

    List<Comment> findByParentCommentId(int parentCommentId);
}