package com.example.alvaBackend.Services;

import com.example.alvaBackend.Dto.CommentDTO;

import com.example.alvaBackend.Dto.CommentReplyResponseDto;
import com.example.alvaBackend.Dto.CommentResponseDto;
import com.example.alvaBackend.Dto.ReplyDTO;
import com.example.alvaBackend.Entities.Comment;
import com.example.alvaBackend.Entities.CommentReply;
import com.example.alvaBackend.Entities.Post;
import com.example.alvaBackend.Entities.User;
import com.example.alvaBackend.Repositories.CommentReplyRepository;
import com.example.alvaBackend.Repositories.CommentRepository;
import com.example.alvaBackend.Repositories.PostRepository;
import com.example.alvaBackend.Repositories.userRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
private final PostRepository postRepository;
private final CommentRepository commentRepository;
private final userRepository userRepository;
    private final CommentReplyRepository commentReplyRepository;


    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          userRepository userRepository, CommentReplyRepository commentReplyRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentReplyRepository = commentReplyRepository;
    }

    public ResponseEntity<Object> createComment(CommentDTO commentRequest) {
        Optional<Post> post = postRepository.findById(commentRequest.getPostId());
        if (post.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Optional<User> user = userRepository.findById(commentRequest.getUserId());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setPost(post.get());
        comment.setUser(user.get());

        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    public List<Comment> getCommentsByUserId(int userId) {
        return commentRepository.findCommentsByUserId(userId);
    }

    public ResponseEntity<Object> updateComment(int commentId, CommentDTO commentRequest) {

        Optional<Comment> existingComment = commentRepository.findById(commentId);
        if (existingComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }

        Comment commentToUpdate = existingComment.get();
        commentToUpdate.setContent(commentRequest.getContent());


        Comment updatedComment = commentRepository.save(commentToUpdate);
        return ResponseEntity.ok(updatedComment);
    }

    @Transactional
    public ResponseEntity<?> deleteComment(int commentId) {
        try {
            if (!commentRepository.existsById(commentId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
            }
            commentRepository.deleteById(commentId);
            commentRepository.flush();
            return ResponseEntity.ok().body("Comment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting comment: " + e.getMessage());
        }
    }
    @Transactional
    public ResponseEntity<?> replyToComment(ReplyDTO replyDTO) {
        // 1. Vérifier le commentaire parent
        Optional<Comment> parentCommentOpt = commentRepository.findById(replyDTO.getParentCommentId());
        if (parentCommentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parent comment not found");
        }

        // 2. Vérifier l'utilisateur
        Optional<User> userOpt = userRepository.findById(replyDTO.getUserId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // 3. Créer la réponse
        CommentReply reply = new CommentReply();
        reply.setContent(replyDTO.getContent());
        reply.setUser(userOpt.get());
        reply.setComment(parentCommentOpt.get());

        CommentReplyResponseDto replyResponseDto = new CommentReplyResponseDto(
                reply.getId(),
                parentCommentOpt.get().getId(),
                reply.getContent()
        );

        // 4. Sauvegarder
        commentReplyRepository.save(reply);

        return ResponseEntity.status(HttpStatus.CREATED).body(replyResponseDto);
    }

    public ResponseEntity<?> getReplies(int parentCommentId) {
        Optional<Comment> parentCommentOpt = commentRepository.findById(parentCommentId);
        if (parentCommentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parent comment not found");
        }

       List<CommentReply> replies = commentReplyRepository.findByComment(parentCommentOpt.get());

        List<CommentReplyResponseDto> replyResponseDtos = replies.stream()
                .map(reply -> new CommentReplyResponseDto(
                        reply.getId(),
                        reply.getComment().getId(),
                        reply.getContent()
                ))
                .toList();


        return ResponseEntity.ok(replyResponseDtos);
    }
}
