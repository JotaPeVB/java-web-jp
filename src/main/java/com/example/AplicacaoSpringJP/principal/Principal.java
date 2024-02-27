package com.example.AplicacaoSpringJP.principal;

import com.example.AplicacaoSpringJP.model.DadosEpisodio;
import com.example.AplicacaoSpringJP.model.DadosSerie;
import com.example.AplicacaoSpringJP.model.DadosTemporada;
import com.example.AplicacaoSpringJP.model.Episodio;
import com.example.AplicacaoSpringJP.service.ConsumoApi;
import com.example.AplicacaoSpringJP.service.ConverteDados;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private ConsumoApi consumoApi = new ConsumoApi();
    private Scanner leitura = new Scanner(System.in);
    private  ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=c5f12354";

    public void exibeMenu(){
        System.out.println("Digite o nome de uma série pra ser buscada");
        var serie = leitura.nextLine();
        var json = consumoApi.obterDados(ENDERECO + serie.replace(" ", "+") + API_KEY);

        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> listaTemporadas = new ArrayList<>();

        for (int temporada = 1; temporada <= dadosSerie.totalTemporadas(); temporada++) {
            json = consumoApi.obterDados(ENDERECO + serie.replace(" ", "+") + "&season=" + temporada + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            listaTemporadas.add(dadosTemporada);
        }

        listaTemporadas.forEach(System.out::println);

        List<DadosEpisodio> listaEpisodios = listaTemporadas.stream()
                        .flatMap(t -> t.episodios().stream())
                        .collect(Collectors.toList());

        listaEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(10)
                .forEach(e -> System.out.println("Melhores ep: " + e.titulo().toUpperCase()));

        List<Episodio> episodios = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream()
                .map(e -> new Episodio(t.numeroTemporada(), e)))
                        .collect(Collectors.toList());

//        System.out.println("A partir de que ano deseja buscar os episodios?");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream().filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                "Episódio: " + e.getTitulo() +
//                                "Data: " + e.getDataLancamento().format(formatador)
//                ));

        System.out.println("Digite o nome de um episódio que deseja buscar");
        String trechoNome = leitura.nextLine();

        Optional<Episodio> buscaEpisodio = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoNome.toUpperCase()))
                .findFirst();

        if (buscaEpisodio.isPresent()) {
            System.out.println("Episódio encontrado com sucesso");
            System.out.println(buscaEpisodio.get());
        } else {
            System.out.println("Episódio não encontrado");
        }

        Map<Integer, Double> avaliacaoTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacaoTemporada);

        DoubleSummaryStatistics estatisticas = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Número de episódios avaliados" + estatisticas.getCount());
        System.out.println("Episodio mais bem avalidado" + estatisticas.getMax());
        System.out.println("Episodio menos bem avaliado" + estatisticas.getMin());
        System.out.println("Média de nota dos episódios da série" + estatisticas.getAverage());
    }
}