package rs.ac.uns.ftn.informatika.rest.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
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
import rs.ac.uns.ftn.informatika.rest.config.Utility;
import rs.ac.uns.ftn.informatika.rest.controller.UserAccountController;
import rs.ac.uns.ftn.informatika.rest.domain.AuthRequest;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.repository.InMemoryUserAccountRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private final InMemoryUserAccountRepository userAccountRepository;

    @Autowired
    private AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JavaMailSender mailSender;
    private UserAccountController userAccountController;

    @Autowired
    public UserAccountServiceImpl(InMemoryUserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Page<UserAccount> findAll(Pageable pageable) {
        return userAccountRepository.findAll(pageable);
    }

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
        content += "<p>You have been inactive since " + userAccount.getLastTimeUsed() + "<p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content, true); // Setting 'true' enables HTML

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

