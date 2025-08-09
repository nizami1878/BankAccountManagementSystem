package com.matrix.BankAccountManagementSystem;

import com.matrix.BankAccountManagementSystem.jwt.JwtService;
import com.matrix.BankAccountManagementSystem.model.entity.security.Authority;
import com.matrix.BankAccountManagementSystem.model.entity.security.User;
import com.matrix.BankAccountManagementSystem.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.TemplateEngine;

import java.util.Set;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@EnableFeignClients(basePackages = "com.matrix.BankAccountManagementSystem")
public class BankAccountManagementSystemApplication implements CommandLineRunner {
	private final TemplateEngine templateEngine;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtService jwtService;


	public static void main(String[] args) {
		SpringApplication.run(BankAccountManagementSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user = userRepository.findByUsername("amad").get();
		String token=jwtService.issueToken(user);
		user.setIssueToken(token);
		userRepository.save(user);
		System.out.println("JWT: " + token);
//		User user1 = new User();
//		user1.setUsername("amad");
//		user1.setPassword(passwordEncoder.encode("123456"));
//		user1.setAccountNonExpired(true);
//		user1.setAccountNonLocked(true);
//		user1.setCredentialsNonExpired(true);
//		user1.setEnabled(true);
//
//		Authority authority=new Authority();
//		authority.setAuthority("ADMIN");
//		authority.setUser(user1);
//
//	  user1.setAuthorities(Set.of(authority));
//	  User userEntity=userRepository.save(user1);
	}
}
