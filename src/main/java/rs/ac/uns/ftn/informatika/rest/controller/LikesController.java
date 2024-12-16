package rs.ac.uns.ftn.informatika.rest.controller;

import jakarta.validation.constraints.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.domain.Likes;
import rs.ac.uns.ftn.informatika.rest.service.LikesService;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikesController {
    @Autowired
    private LikesService likesService;

    @GetMapping
    public List<Likes> getLikes() {
        return likesService.getAllLikes();
    }
}
