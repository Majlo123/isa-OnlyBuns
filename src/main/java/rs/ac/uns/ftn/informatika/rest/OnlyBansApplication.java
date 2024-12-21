package rs.ac.uns.ftn.informatika.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class OnlyBansApplication {

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().buildValidatorFactory();
		return validatorFactory.getValidator();
	}

	@GetMapping("/something")
	public ResponseEntity<String> getMessage(){
		return ResponseEntity.ok().body("Just checking");
	}

	public static void main(String[] args) {
		SpringApplication.run(OnlyBansApplication.class, args);
	}
}
