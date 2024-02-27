package com.example.AplicacaoSpringJP;

import com.example.AplicacaoSpringJP.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AplicacaoSpringJpApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AplicacaoSpringJpApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
