package rs.ac.uns.ftn.informatika.rest.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.rest.domain.Post;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.rest.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.rest.repository.PostRepository;
import rs.ac.uns.ftn.informatika.rest.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Isolation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;


    public List<Post> getAllPosts() {
        return postRepository.findAllPostsWithSortedComments();
    }


    public Post updatePost(Long postId, PostDTO postDTO) {
        Post post = getPostById(postId);
        post.setDescription(postDTO.getDescription());

        return postRepository.save(post);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findAllByUserIdAndDeletedFalse(userId);
    }

    public Post createPost(PostDTO postDTO) {
        Long userId = postDTO.getUserId();
        Post post = new Post(postDTO.getTitle(), postDTO.getDescription(), postDTO.getImageUrl(), userId, postDTO.getLongitude(), postDTO.getLatitude(), postDTO.getDateOfCreation());

        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        post.setDeleted(true);
        postRepository.save(post);
    }

    @Transactional(readOnly = false)
    public void likePost(Long postId) {

        Post post = postRepository.findByIdWithLock(postId).orElseThrow(() -> new EntityNotFoundException("Post with id: " + postId + " not found!!!"));

        //For testing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        post.setLikes(post.getLikes() + 1);

        postRepository.save(post);
    }

    public Comment addComment(Long postId, CommentDTO commentDTO) {
        Post post = getPostById(postId);
        Comment comment = new Comment(commentDTO.getContent(), commentDTO.getUserId(), commentDTO.getCreatedAt());

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(2);
        int commentCount = postRepository.countCommentsInLastHour(comment.getUserId(), oneHourAgo);

        if (commentCount >= 60) {
            throw new IllegalArgumentException("You have reached the limit of 60 comments per hour.");
        }
        post.getComments().add(comment);
        postRepository.save(post);
        return comment;
    }
}
