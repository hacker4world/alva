package com.example.alvaBackend.Repositories;

import com.example.alvaBackend.Entities.Comment;
import com.example.alvaBackend.Entities.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply, Integer> {
    List<CommentReply> findByComment(Comment comment);
}
