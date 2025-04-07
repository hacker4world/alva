package com.example.alvaBackend.Controllers;

import com.example.alvaBackend.Dto.PostDto;
import com.example.alvaBackend.Dto.PostResponseDto;
import com.example.alvaBackend.Dto.UpdatePostDto;
import com.example.alvaBackend.Entities.Post;
import com.example.alvaBackend.Services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService=postService;
    }

    @PostMapping("/addPost")
    public ResponseEntity<?> createPost(@RequestBody PostDto post) {
        return postService.addPost(post);
    }

    @PostMapping("/{id}/addAttachment")
    public ResponseEntity<String> addAttachment(@PathVariable int id,@RequestParam("attachment") MultipartFile attachment) {
        return postService.addImage(id,attachment);
    }

    @PutMapping("/updatePost/{id}")
    public ResponseEntity<String> updatePost(@PathVariable int id,@RequestBody UpdatePostDto editedPost) {
        return postService.updatePost(id,editedPost);
    }

    @PutMapping("/{id}/updateAttachment")
    public ResponseEntity<String> updateAttachment(@PathVariable int id,@RequestParam(value = "attachment", required = false)  MultipartFile image) {
        return postService.updateImage(id,image);
    }

    @PutMapping("/archivePost/{id}")
    public ResponseEntity<String> archivePost(@PathVariable int id) {
        return postService.archivePost(id);
    }


    @DeleteMapping("delete/{id}")
    public String deletePost(@PathVariable Integer id) {
        postService.deletePostById(id);
        return "Post with ID " + id + " has been deleted.";
    }

    @GetMapping("/getBy/{id}")
    public Post getPost(@PathVariable Integer id) {
        return postService.getPostById(id);
    }

    @GetMapping("/non-archived")
    public ResponseEntity<?> getNonArchivedPosts() {
        List<Post> posts = postService.getNonArchivedPosts();

        if (posts.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Aucun post non archivé trouvé.");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/byDepartment/{departmentId}")
    public List<PostResponseDto> getPostsByDepartment(@PathVariable int departmentId) {
        return postService.getPostsByDepartment(departmentId);
    }
    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable int userId) {
        return postService.getPostsByUserId(userId);
    }

}