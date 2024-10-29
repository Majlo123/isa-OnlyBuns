package rs.ac.uns.ftn.informatika.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.config.Utility;
import rs.ac.uns.ftn.informatika.rest.domain.AuthRequest;
import rs.ac.uns.ftn.informatika.rest.domain.UserAccount;
import rs.ac.uns.ftn.informatika.rest.dto.UserAccountDTO;
import rs.ac.uns.ftn.informatika.rest.service.JwtService;
import rs.ac.uns.ftn.informatika.rest.service.UserAccountService;

import javax.print.attribute.standard.Media;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.awt.*;
import java.util.Collection;
@RestController
@RequestMapping("/api/userAccount")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;





    @Operation(description = "Get all users", method = "GET")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserAccount>> getAllUsers() {
        Collection<UserAccount> userAccounts = userAccountService.findAll();
        return new ResponseEntity<Collection<UserAccount>>(userAccounts, HttpStatus.OK);
    }

    @Operation(description = "Create new user", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserAccount.class))}),
            @ApiResponse(responseCode = "409", description = "Not possible to create new greeting when given id is not null or empty",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, path = "/register")
    public ResponseEntity<UserAccount> createUser(@Valid @RequestBody UserAccountDTO userAccountDto, HttpServletRequest request) throws ConstraintViolationException {
        UserAccount newAccount = null;
        try{
            newAccount = userAccountService.create(userAccountDto, request);


            return  new ResponseEntity<UserAccount>(newAccount, HttpStatus.CREATED);
        } catch (Exception e){
            return  new ResponseEntity<UserAccount>(HttpStatus.CONFLICT);
        }
    }
    @PostMapping(path = "/login")
    public String login(@RequestBody AuthRequest credentials) {
        return userAccountService.verify(credentials);
    }
    @Operation(description = "Delete user", method = "DELETE")
    @ApiResponses(value = { @ApiResponse(responseCode = "404", description = "Greeting not found", content = @Content),
            @ApiResponse(responseCode = "204", description = "User Account successfully deleted", content = @Content) } )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserAccount> deleteUser(@Parameter(description = "user id", required = true) @PathVariable("id") Long id) {
        UserAccount userAccount = userAccountService.delete(id);
        if(userAccount == null){
            return new ResponseEntity<UserAccount>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserAccount>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userAccountService.verifyVerificationCode(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
}
