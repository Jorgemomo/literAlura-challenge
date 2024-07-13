package com.aluracursos.literalura_challenge;

import com.aluracursos.literalura_challenge.main.Main;
import com.aluracursos.literalura_challenge.repository.IRepositoryAuthors;
import com.aluracursos.literalura_challenge.repository.IRepositoryBooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraChallengeApplication implements CommandLineRunner {

	@Autowired
	private IRepositoryAuthors autoresRepository;
	@Autowired
	private IRepositoryBooks librosRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraluraChallengeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main principal = new Main(autoresRepository, librosRepository);
		principal.menu();
	}
}
