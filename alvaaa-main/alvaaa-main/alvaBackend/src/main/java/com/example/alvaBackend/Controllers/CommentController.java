package com.example.alvaBackend.Controllers;

import com.example.alvaBackend.Dto.CommentDTO;

import com.example.alvaBackend.Dto.ReplyDTO;
import com.example.alvaBackend.Entities.Comment;
import com.example.alvaBackend.Services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping("/createComment")
    public ResponseEntity<?> createComment( @RequestBody CommentDTO commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(commentRequest));
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable int userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }


    @PutMapping("update/{commentId}")
    public ResponseEntity<Object> updateComment(
            @PathVariable int commentId,
            @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(commentId, commentDTO);
    }

    @DeleteMapping("delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId) {
        return commentService.deleteComment(commentId);
    }

    @PostMapping("/reply")
    public ResponseEntity<?> replyToComment(@RequestBody ReplyDTO replyDTO) {
        return commentService.replyToComment(replyDTO);
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<?> getReplies(@PathVariable int commentId) {
        return commentService.getReplies(commentId);
    }


}