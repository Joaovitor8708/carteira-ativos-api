package br.com.carteira.carteira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarteiraApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarteiraApplication.class, args);
	}

}
