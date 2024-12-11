package rs.ac.uns.ftn.informatika.rest;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.rest.domain.Post;
import rs.ac.uns.ftn.informatika.rest.repository.PostRepository;
import rs.ac.uns.ftn.informatika.rest.service.PostService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class PostTests {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    public void tearDown() {
        postRepository.deleteAll();
    }

    @Test
    public void testConcurrentLikePost() throws InterruptedException {
        postRepository.deleteAll();

        Post post = new Post();
        post.setUserId(-100L);
        post.setDescription("Test Post");
        post = postRepository.save(post);

        long postId = post.getId();

        // Simulacija konkurencije
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> postService.likePost(postId));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {

        }

        // Provera rezultata
        Post updatedPost = postService.getPostById(postId);

        System.out.println("Post Id: " + updatedPost.getId());
        System.out.println("Post description: " + updatedPost.getDescription());
        System.out.println("Final number of likes: " + updatedPost.getLikes());
        assertEquals(5, updatedPost.getLikes());
    }
}
