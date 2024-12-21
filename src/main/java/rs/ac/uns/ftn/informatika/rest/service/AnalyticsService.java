package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.dto.AnalyticsResponse;
import rs.ac.uns.ftn.informatika.rest.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.rest.repository.PostRepository;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;

import java.util.Date;

@Service
public class AnalyticsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private InMemoryUserAccountRepository InMemoryUserAccountRepository;

    public AnalyticsResponse getAnalytics(Date startDate, Date endDate) {
        long postCount = postRepository.countPostsBetween(startDate, endDate);
        long commentCount = commentRepository.countCommentsBetween(startDate, endDate);

        long totalUsers = InMemoryUserAccountRepository.count();
        long usersWithPosts = InMemoryUserAccountRepository.countUsersWithPosts();
        long usersWithComments = InMemoryUserAccountRepository.countUsersWithComments();
        long inactiveUsers = totalUsers - (usersWithPosts + usersWithComments);

        return new AnalyticsResponse(postCount, commentCount, totalUsers, usersWithPosts, usersWithComments, inactiveUsers);
    }
}