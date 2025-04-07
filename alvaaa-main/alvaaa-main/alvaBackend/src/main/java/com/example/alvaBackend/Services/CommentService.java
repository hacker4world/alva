package com.example.alvaBackend.Services;

import com.example.alvaBackend.Dto.CommentDTO;

import com.example.alvaBackend.Dto.ReplyDTO;
import com.example.alvaBackend.Entities.Comment;
import com.example.alvaBackend.Entities.Post;
import com.example.alvaBackend.Entities.User;
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


    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          userRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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
        Comment reply = new Comment();
        reply.setContent(replyDTO.getContent());
        reply.setUser(userOpt.get());
        reply.setPost(parentCommentOpt.get().getPost()); // Même post que le parent
        reply.setParentComment(parentCommentOpt.get());

        // 4. Sauvegarder
        Comment savedReply = commentRepository.save(reply);

        // 5. Optionnel: ajouter la réponse à la liste du parent
        parentCommentOpt.get().getReplies().add(savedReply);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
    }

    public ResponseEntity<?> getReplies(int parentCommentId) {
        if (!commentRepository.existsById(parentCommentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parent comment not found");
        }

        List<Comment> replies = commentRepository.findByParentCommentId(parentCommentId);
        return ResponseEntity.ok(replies);
    }
}
