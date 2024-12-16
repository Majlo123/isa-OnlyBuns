package rs.ac.uns.ftn.informatika.rest.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.rest.config.Utility;
import rs.ac.uns.ftn.informatika.rest.controller.UserAccountController;
import rs.ac.uns.ftn.informatika.rest.domain.AuthRequest;
import rs.ac.uns.ftn.informatika.rest.domain.Follow;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.repository.FollowRepository;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private final InMemoryUserAccountRepository userAccountRepository;

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private JwtService jwtService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JavaMailSender mailSender;
    private UserAccountController userAccountController;
    @Autowired
    private FollowInfoService followInfoService;

    private static final int FOLLOW_LIMIT_PER_MINUTE = 50;

    @Autowired
    public UserAccountServiceImpl(InMemoryUserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }
    public boolean isFollowing(Long currentUserId, Long userId) {
        return followRepository.existsByFollowerIdAndFolloweeId(currentUserId, userId);
    }

    @Transactional(readOnly = false)
    public void followUser(Long currentUserId, Long targetUserId) {
        if (currentUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("Ne možete pratiti sami sebe");
        }

        if (followRepository.existsByFollowerIdAndFolloweeId(currentUserId, targetUserId)) {
            return;
        }

        UserAccount targetUser = userAccountRepository.findByIdWithLock(targetUserId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        try {
            Thread.sleep(1000); // Simulacija
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Follow follow = new Follow();
        follow.setFollowerId(currentUserId);
        follow.setFolloweeId(targetUserId);
        follow.setFollowedAt(LocalDateTime.now());
        followRepository.save(follow);

        targetUser.setFollowersCount(targetUser.getFollowersCount() + 1);
        userAccountRepository.save(targetUser);

        // Forsiranje sinhronizacije s bazom
        entityManager.flush();
    }
    @Scheduled(cron = "0 0 0 L * ?") 
    public void markUnverifiedAccountsAsDeleted() {
        List<UserAccount> unverifiedAccounts = userAccountRepository.findAllByIsEnabledFalseAndIsDeletedFalse();

        for (UserAccount account : unverifiedAccounts) {
            account.setDeleted(true);
        }

        userAccountRepository.saveAll(unverifiedAccounts);

        System.out.println("Marked " + unverifiedAccounts.size() + " unverified accounts as deleted.");
    }




    @Transactional
    public void unfollowUser(Long currentUserId, Long targetUserId) {
        // Provera da li prati
        if (!followRepository.existsByFollowerIdAndFolloweeId(currentUserId, targetUserId)) {
            // Ne prati ovog korisnika
            return;
        }

        // Obriši Follow zapis
        followRepository.deleteByFollowerIdAndFolloweeId(currentUserId, targetUserId);

        // Smanji broj pratilaca kod targetUserId
        UserAccount userToUnfollow = userAccountRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));

        // Osigurati da ne padnemo u negativan broj
        userToUnfollow.setFollowersCount(Math.max(0, userToUnfollow.getFollowersCount() - 1));
        userAccountRepository.save(userToUnfollow);
    }
    @Override
    public Page<UserAccount> findAll(Pageable pageable) {
        return userAccountRepository.findAll(pageable);
    }
    //@Override
    //public UserAccount findByEmail(String email) {return userAccountRepository.findByEmail(email);}
    @Override
    public UserAccount findById(Long id) {
        return userAccountRepository.findById(id).orElse(null);
    }
    @Override
    public String getUsernameById(Long userId) {
        UserAccount user = userAccountRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getFirstName() + " " + user.getLastName(); // Ili prilagodite prikaz korisničkog imena
        }
        return null;
    }
    @Override
    @Transactional(readOnly = false,isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRES_NEW)
    public UserAccount create(UserAccountDTO accountDTO, HttpServletRequest request) throws Exception {

            if (userAccountRepository.findByEmail(accountDTO.getEmail()) != null) {
                throw new Exception("Email already exists");
            } else {
                accountDTO.setPassword(encoder.encode(accountDTO.getPassword()));

                UserAccount savedAcc = new UserAccount(accountDTO);
                savedAcc.setEnabled(false);
                String randomCode = RandomStringUtils.randomAlphanumeric(64);
                savedAcc.setVerificationCode(randomCode);
                userAccountRepository.save(savedAcc);
                sendVerificationEmail(savedAcc, request);
                return savedAcc;
            }

    }

    @Override
    public Collection<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }



    @Override
    public void sendVerificationEmail(UserAccount savedAcc, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String toAddress = savedAcc.getEmail();
        String fromAddress = "onlybunsteam@gmail.com";
        String subject = "Verification Email";
        String senderName = "OnlyBuns Team";
        String content = "<p>Dear "+savedAcc.getFirstName() + ",<p>";
        content += "<p>Please click the link below to verify your account</p>";
        String siteUrl = Utility.getSiteURL(request) + "/api/userAccount/verify?code=" + savedAcc.getVerificationCode();
        content += "<h3><a href=\"" + siteUrl + "\">VERIFY</a></h3>";
        content += "<p>The OnlyBuns Team</p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true); // Setting 'true' enables HTML

        mailSender.send(message);

    }

    private void sendInactivityEmail(UserAccount userAccount) throws MessagingException, UnsupportedEncodingException {
        String toAddress = userAccount.getEmail();
        String fromAddress = "onlybunsteam@gmail.com";
        String subject = "7 Day Inactivity Summary";
        String senderName = "OnlyBuns Team";
        String content = "<p>Dear "+userAccount.getFirstName() + ",<p>";
        content += "<p>You have been inactive since " + userAccount.getLastTimeUsed() + "<p><br>";
        content += "<p>In the last 7 days you received " + (long) followInfoService.getAllForSevenDaysForUser(userAccount.getId()).size() + " followers!<p>";
        content += "<p>Also your posts have gotten " + 5 + " likes in the last 7 days!";
        content += "<p>Dive deep again in the world of bunnies: <a href= https://dailybunny.org/>BunnyWorld</a><p>";
        content += "<p>Congratulations!<p>";
        content += "<br><p>The OnlyBuns Team</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    //@Scheduled(cron = "0 0 0 */7 * *")
    //@Scheduled(fixedRate = 60 * 1000)
    private void checkInactivity() throws MessagingException, UnsupportedEncodingException {
        List<UserAccount> userAccounts = userAccountRepository.findAll();

        for (UserAccount userAccount : userAccounts) {
            if (ChronoUnit.DAYS.between(
                    userAccount.getLastTimeUsed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    LocalDate.now()) >= 7) {
                sendInactivityEmail(userAccount);
            }
        }
    }
    @Override
    public UserAccount delete(Long id) {
        UserAccount deletedAcc = userAccountRepository.findById(id).orElse(null);
        userAccountRepository.deleteById(id);
        return deletedAcc;
    }

    @Override
    public UserAccount update(UserAccountDTO userAccountDto, Long id) throws Exception {
        return userAccountRepository.findById(id)
                .map(existingUserAccount -> {
                    existingUserAccount.setFirstName(userAccountDto.getFirstName());
                    existingUserAccount.setLastName(userAccountDto.getLastName());
                    existingUserAccount.setEmail(userAccountDto.getEmail());
                    existingUserAccount.setPassword(userAccountDto.getPassword());
                    existingUserAccount.setFollowersCount(userAccountDto.getFollowersCount());
                    existingUserAccount.setAddress(userAccountDto.getAddress().toString());
                    existingUserAccount.setLastTimeUsed(userAccountDto.getLastTimeUsed());
                    return userAccountRepository.save(existingUserAccount);
                })
                .orElseThrow(() -> new Exception("UserAccount not found with id: " + id));
    }


    public List<UserAccount> searchByFirstName(String firstName) {
        return userAccountRepository.findByFirstNameContaining(firstName);
    }
    @Override
    public String getEmailById(long userId) {
       Optional<UserAccount> userAcc = userAccountRepository.findById(userId);
       return userAcc.map(UserAccount::getEmail).orElse(null);
    }
    @Override
    @Transactional(readOnly = false)
    public String verify(AuthRequest credentials) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));


        if (authentication.isAuthenticated()) {

            UserAccount user = userAccountRepository.findByEmail(credentials.getUsername());

            if (user != null && user.isEnabled()) {
                return jwtService.generateToken(user.getEmail(), user.getId(), user.getRole());
            }
        }
        return "Failure";
    }

    @Override
    @Transactional(readOnly = false)
    public boolean verifyVerificationCode(String verificationCode) {
        UserAccount user = userAccountRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userAccountRepository.save(user);

            return true;
        }

    }



    public List<UserAccount> searchByLastName(String lastName) {
        return userAccountRepository.findByLastNameContaining(lastName);
    }

    public List<UserAccount> searchByEmail(String email) {
        return userAccountRepository.findByEmailContaining(email);
    }

    public List<UserAccount> searchByPostCount(int minPosts, int maxPosts) {
        return userAccountRepository.findByPostCountBetween(minPosts, maxPosts);
    }

    public List<UserAccount> sortByFollowingCount() {
        return userAccountRepository.findAllSortedByFollowingCount();
    }

    public List<UserAccount> sortByEmail() {
        return userAccountRepository.findAllSortedByEmail();
    }
}

