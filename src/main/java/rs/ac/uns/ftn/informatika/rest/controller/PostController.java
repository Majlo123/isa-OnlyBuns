package rs.ac.uns.ftn.informatika.rest.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.Post;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.rest.service.LikesService;
import rs.ac.uns.ftn.informatika.rest.service.PostService;
import rs.ac.uns.ftn.informatika.rest.service.UserAccountService;
import rs.ac.uns.ftn.informatika.rest.utils.RateLimiter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private UserAccountService userAccountService;

    private final RateLimiter rateLimiter = new RateLimiter();

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable Long userId) {
        return postService.getPostsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostDTO postDTO) {

        if (postDTO.getImageBase64() != null && !postDTO.getImageBase64().isEmpty()) {
            try {
                // Decode Base64
                byte[] imageData = Base64.getDecoder().decode(postDTO.getImageBase64().split(",")[1]);


                String fileName = UUID.randomUUID().toString() + ".jpg";  // Change extension if needed
                Path imagePath = Paths.get("src/main/resources/static/images/posts");

                // Ensure the directory exists
                Files.createDirectories(imagePath);

                // Create file path and save image
                Path filePath = imagePath.resolve(fileName);
                Files.write(filePath, imageData);

                // Set the image URL in the Post entity
                postDTO.setImageUrl("images/posts/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).build();
            }
        }

        return ResponseEntity.ok(postService.createPost(postDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.updatePost(id, postDTO));
    }

    @PostMapping("/{id}/{userId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id, @PathVariable Long userId) {
        postService.likePost(id);
        UserAccount userLiked = userAccountService.findById(userId);
        Post likedPost = postService.getPostById(id);
        likesService.likePost(likedPost, userLiked);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/following/{userId}")
    public ResponseEntity<List<Post>> getPostsByFollowing(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByFollowing(userId);
        return ResponseEntity.ok(posts);
    }
    @PostMapping("/{id}/comments")
    public ResponseEntity<String> addComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        try {
            if(commentDTO.getUserId() != 0){
                String identifier = request.getSession().getAttribute("email") != null
                        ? (String) request.getSession().getAttribute("email")
                        : request.getRemoteAddr(); // Use IP if no email

                if (!rateLimiter.isRequestAllowed(identifier)) {
                    return new ResponseEntity<>("Too many requests. Please try again later.", HttpStatus.TOO_MANY_REQUESTS);
                }
                postService.addComment(id, commentDTO);


                return ResponseEntity.ok().build();
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
