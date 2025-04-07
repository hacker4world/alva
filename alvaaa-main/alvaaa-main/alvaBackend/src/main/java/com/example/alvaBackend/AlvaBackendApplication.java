package com.example.alvaBackend;

import com.example.alvaBackend.Entities.Admin;
import com.example.alvaBackend.Repositories.adminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
//@EnableScheduling
public class AlvaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlvaBackendApplication.class, args);
	}
	//@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx, PasswordEncoder passwordEncoder, adminRepository adminRepository) {
		return args -> {

			String password = passwordEncoder.encode("12345678");

			Admin admin = new Admin("12345678","admin","samir","benYahia","samir@gmail.com",password,"03rue limousin","20055555","active");

			adminRepository.save(admin);

			System.out.println("Admin created");

		};
	}

}
