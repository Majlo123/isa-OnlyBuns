package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.dto.AnalyticsResponse;
import rs.ac.uns.ftn.informatika.rest.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.rest.repository.PostRepository;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;

import java.time.LocalDate;
import java.util.Date;

@Service
public class AnalyticsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private InMemoryUserAccountRepository userAccountRepository;

    public AnalyticsResponse getAnalytics(LocalDate startDate, LocalDate endDate) {
        // Ukupan broj korisnika
        long totalUsers = userAccountRepository.count();

        // Korisnici koji su objavljivali
        long usersWithPosts = userAccountRepository.countUsersWithPosts();

        // Korisnici koji su komentarisali
        long usersWithComments = userAccountRepository.countUsersWithComments();

        // **Jedinstveni aktivni korisnici** (objavljivali ili komentarisali)
        long uniqueActiveUsers = userAccountRepository.countUniqueUsersWithPostsOrComments();

        // **Neaktivni korisnici** (oni koji nisu ni objavili ni komentarisali)
        long inactiveUsers = totalUsers - uniqueActiveUsers;

        // Procenti
        double postingPercentage = ((double) usersWithPosts / totalUsers) * 100;
        double commentingPercentage = ((double) usersWithComments / totalUsers) * 100;
        double inactivePercentage = ((double) inactiveUsers / totalUsers) * 100;

        // Povratni odgovor
        return new AnalyticsResponse(
                postRepository.countPostsBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)),
                commentRepository.countCommentsBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)),
                totalUsers,
                usersWithPosts,
                usersWithComments,
                postingPercentage,
                commentingPercentage,
                inactivePercentage
        );
    }
}
