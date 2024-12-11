package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.Post;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.rest.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.rest.repository.PostRepository;
import rs.ac.uns.ftn.informatika.rest.repository.FollowRepository; // Dodato
import rs.ac.uns.ftn.informatika.rest.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FollowRepository followRepository; // Dodato

    public List<Post> getAllPosts() {
        return postRepository.findAllByDeletedFalse();
    }

    public Post updatePost(Long postId, PostDTO postDTO) {
        Post post = getPostById(postId);
        post.setDescription(postDTO.getDescription());
        return postRepository.save(post);
    }

    public List<Post> getPostsByFollowing(Long userId) {
        // Pronađi ID-ove korisnika koje trenutni korisnik prati
        List<Long> followingUserIds = followRepository.findFolloweeIdsByFollowerId(userId);

        // Pronađi postove koje su ti korisnici napravili
        return postRepository.findByUserIdIn(followingUserIds);
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

    public void likePost(Long postId) {
        Post post = getPostById(postId);
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);
    }

    public Comment addComment(Long postId, CommentDTO commentDTO) {
        Post post = getPostById(postId);
        Comment comment = new Comment(commentDTO.getContent(), commentDTO.getUserId());
        post.getComments().add(comment);
        postRepository.save(post);
        return comment;
    }
}
