package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.Likes;
import rs.ac.uns.ftn.informatika.rest.domain.Post;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.repository.LikesRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikesService {
    @Autowired
    private LikesRepository likesRepository;


    public List<Likes> getAllLikes(){
        return likesRepository.findAll();
    }

    public Likes likePost(Post likedPost, UserAccount userLiked) {
        LocalDateTime now = LocalDateTime.now();
        Likes likes = new Likes();
        likes.setPost(likedPost);
        likes.setUser(userLiked);
        likes.setDateLiked(now);
        likesRepository.save(likes);
        return likes;
    }
}
