package com.example.AplicacaoSpringJP;

import com.example.AplicacaoSpringJP.model.DadosSerie;
import com.example.AplicacaoSpringJP.service.ConsumoApi;
import com.example.AplicacaoSpringJP.service.ConverteDados;
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
		ConsumoApi consumoApi = new ConsumoApi();

		String json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=c5f12354");
		System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);

		System.out.println(dadosSerie);
	}
}
