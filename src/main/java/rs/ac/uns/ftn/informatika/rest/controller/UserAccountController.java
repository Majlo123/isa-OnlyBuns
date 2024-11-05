package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.AuthRequest;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.service.UserAccountService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/userAccount")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @Operation(description = "Get all users", method = "GET")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserAccount>> getAllUsers() {
        Collection<UserAccount> userAccounts = userAccountService.findAll();
        return new ResponseEntity<>(userAccounts, HttpStatus.OK);
    }

    @Operation(description = "Create new user", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserAccount.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, path = "/register")
    public ResponseEntity<UserAccount> createUser(@Valid @RequestBody UserAccountDTO userAccountDto) {
        try {
            UserAccount newAccount = userAccountService.create(userAccountDto);
            return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody AuthRequest credentials) {
        return userAccountService.verify(credentials);
    }

    @Operation(description = "Delete user", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "204", description = "User Account successfully deleted", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserAccount> deleteUser(@Parameter(description = "user id", required = true) @PathVariable("id") Long id) {
        UserAccount userAccount = userAccountService.delete(id);
        if (userAccount == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // New search and sort endpoints

    @Operation(description = "Search users by first name", method = "GET")
    @GetMapping(value = "/search/firstName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAccount>> searchByFirstName(@RequestParam("firstName") String firstName) {
        List<UserAccount> users = userAccountService.searchByFirstName(firstName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(description = "Search users by last name", method = "GET")
    @GetMapping(value = "/search/lastName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAccount>> searchByLastName(@RequestParam("lastName") String lastName) {
        List<UserAccount> users = userAccountService.searchByLastName(lastName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(description = "Search users by email", method = "GET")
    @GetMapping(value = "/search/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAccount>> searchByEmail(@RequestParam("email") String email) {
        List<UserAccount> users = userAccountService.searchByEmail(email);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(description = "Search users by post count range", method = "GET")
    @GetMapping(value = "/search/postCount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAccount>> searchByPostCount(@RequestParam("min") int min, @RequestParam("max") int max) {
        List<UserAccount> users = userAccountService.searchByPostCount(min, max);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(description = "Sort users by following count", method = "GET")
    @GetMapping(value = "/sort/followingCount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAccount>> sortByFollowingCount() {
        List<UserAccount> users = userAccountService.sortByFollowingCount();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(description = "Sort users by email", method = "GET")
    @GetMapping(value = "/sort/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAccount>> sortByEmail() {
        List<UserAccount> users = userAccountService.sortByEmail();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
