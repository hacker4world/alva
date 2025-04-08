package com.example.alvaBackend.Services;

import com.example.alvaBackend.Dto.CommentResponseDto;
import com.example.alvaBackend.Dto.PostDto;
import com.example.alvaBackend.Dto.PostResponseDto;
import com.example.alvaBackend.Dto.UpdatePostDto;
import com.example.alvaBackend.Entities.Manager;
import com.example.alvaBackend.Entities.Post;
import com.example.alvaBackend.Entities.User;
import com.example.alvaBackend.Repositories.managerRepository;
import com.example.alvaBackend.Repositories.PostRepository;
import com.example.alvaBackend.Repositories.userRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {


    private final PostRepository postRepository;
    private final userRepository userRepository;

    public PostService(userRepository userRepository,PostRepository postRepository){
        this.userRepository=userRepository;
        this.postRepository=postRepository;
    }

    public ResponseEntity<Map<String, Integer>> addPost(PostDto postDto) {
        Optional<User> user = userRepository.findById(postDto.getUser_id());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Post newPost = new Post(
                postDto.getTitle(),
                postDto.getDescription(),
                postDto.getContent(),
                LocalDateTime.now(),
                postDto.getCategory(),
                "posted",
                postDto.getAttachment(),
                user.get()
        );

        Post post = postRepository.save(newPost);

        Map<String, Integer> response = new HashMap<>();
        response.put("pub_id", post.getPub_id());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> addImage(int id, MultipartFile attachment) {
        Optional<Post> optionalPost = postRepository.findById(id);

        try {
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                post.setAttachment(attachment.getBytes());
                postRepository.save(post);
                return ResponseEntity.status(200).body("Success!!");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error!");
        }
        throw new RuntimeException("Post not existed!");
    }


    public ResponseEntity<String> updatePost(int id, UpdatePostDto updatedPost) {
        try {
            Optional<Post> post = postRepository.findById(id);
            if (post.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Post with id " + id + " not found!");
            } else {
                Post existingPost = post.get();
                existingPost.setTitle(updatedPost.getTitle());
                existingPost.setDescription(updatedPost.getDescription());
                existingPost.setContent(updatedPost.getContent());
                existingPost.setLast_modification_date(LocalDateTime.now());
                existingPost.setCategory(updatedPost.getCategory());
                existingPost.setStatus("edited");
                existingPost.setAttachment(updatedPost.getAttachment());

                postRepository.save(existingPost);
                return ResponseEntity.status(200).body("Post updated successfully!");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    public ResponseEntity<String> updateImage(int id, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                Optional<Post> post = postRepository.findById(id);
                if (post.isPresent()) {
                    Post existingPost = post.get();
                    existingPost.setAttachment(image.getBytes());
                    postRepository.save(existingPost);
                    return ResponseEntity.status(200).body("Image updated successfully");
                } else {
                    return ResponseEntity.status(404).body("Post not found");
                }
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error updating image");
            }
        } else {
            return ResponseEntity.status(400).body("No image provided");
        }
    }

    public ResponseEntity<String> archivePost(int pub_id) {
        try {
            Optional<Post> optionalPost = postRepository.findById(pub_id);
            if (optionalPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Post with id " + pub_id + " not found!");
            } else {
                Post post = optionalPost.get();
                post.setStatus("archived");
                postRepository.save(post);
                return ResponseEntity.ok("Post archived successfully!!");
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public void deletePostById(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.deleteById(id);
        } else {
            throw new RuntimeException("Post with id " + id + " not found");
        }
    }

    public Post getPostById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post with ID " + id + " not found"));
    }
   /* @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void archiveOldPosts() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar.getTime();


        List<Post> postsToArchive = postRepository.findPostsToArchive(tenDaysAgo);

        // Archiver chaque post
        for (Post post : postsToArchive) {
            postRepository.archivePostById(post.getId());
            System.out.println("✅ Post archivé : " + post.getTitle());
        }
    }*/

    /* @Transactional
       @Scheduled(fixedRate=18000)
       public void archiveOldPosts() {

           Calendar calendar = Calendar.getInstance();
           calendar.add(Calendar.SECOND, -30);
           Date testTime = calendar.getTime();


           List<Post> postsToArchive = postRepository.findPostsToArchive(testTime);


           for (Post post : postsToArchive) {
               postRepository.archivePostById(post.getId());
               System.out.println("✅ Post archivé : " + post.getTitle());
           }
       }
   */
    public List<Post> getNonArchivedPosts() {
        return postRepository.findAllNonArchivedPosts();
    }

    public List<PostResponseDto> getPostsByDepartment(int departmentId) {
        List<Post> posts = postRepository.findPostsByDepartmentId(departmentId);

        List<PostResponseDto> responses = new ArrayList<>();

        posts.forEach(post -> {
            List<CommentResponseDto> commentsResponse = new ArrayList<>();

            post.getComments().forEach(comment -> {

                commentsResponse.add(new CommentResponseDto(comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getUser().getFirstName()));
            });

            responses.add(new PostResponseDto(
                    post.getPub_id(), post.getTitle(), post.getDescription(), post.getContent(), post.getPublication_date(), post.getLast_modification_date(),
                    post.getCategory(), post.getStatus(), post.getAttachment(), post.getLikeCount(), commentsResponse
            ));

        });

        return responses;

    }

    public List<Post> getPostsByUserId(int userId) {

        return postRepository.findPostsByUserId(userId);
    }

    //ReactManagement




}
