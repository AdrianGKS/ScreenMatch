package br.com.adrian.screenmatch.main;

import br.com.adrian.screenmatch.model.DadosEpisodio;
import br.com.adrian.screenmatch.model.DadosSerie;
import br.com.adrian.screenmatch.model.DadosTemporada;
import br.com.adrian.screenmatch.model.Episodio;
import br.com.adrian.screenmatch.service.ConsumoAPI;
import br.com.adrian.screenmatch.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final Scanner in = new Scanner(System.in);
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();
    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private static final String APIKEY = "&apikey=c8d98969";

    public void exibeMenu() throws JsonProcessingException {
        System.out.println("Digite o nome da série para procurá-la: ");
        var nomeSerie = in.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + APIKEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        System.out.println("---------------------------------------");

        List<DadosTemporada> temporadas =  new ArrayList<>();

        for(int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + APIKEY);
            DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(temporada);
        }
        temporadas.forEach(System.out::println);

        System.out.println("---------------------------------------");

//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodios =  temporadas.get(i).episodios();
//            for (int j = 0; j <episodios.size(); j++) {
//                System.out.println(episodios.get(j).titulo());
//                              ||
//            }                 ||
//        }                     \/
        //       lambda - (parametro -> expressão/função)
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        System.out.println("---------------------------------------");

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                //.collect(Collectors.toList()); lista mutável
                .toList(); //lista imutável
        System.out.println("Top 10 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                //.peek(e -> System.out.println("Primeiro Filtro(N/A) "))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                //.peek(e -> System.out.println("Ordenação " + e))
                .limit(10)
                //.peek(e -> System.out.println("Limite " + e))
                .map(e -> e.titulo().toUpperCase())
                //.peek(e -> System.out.println("Mapeamento "))
                .forEach(System.out::println);
        System.out.println("---------------------------------------");

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                        ).collect(Collectors.toList());

        episodios.forEach(System.out::println);
        System.out.println("---------------------------------------");

        System.out.println("Digite um trecho de um título de um episódio: ");
        var trechoTitulo = in.nextLine();

        Optional<Episodio> optionalEpisodio = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if (optionalEpisodio.isPresent()) {
            System.out.println("Episório encontrado!");
            System.out.println("Temporada: " + optionalEpisodio.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }
        System.out.println("---------------------------------------");

        System.out.println("A partir de que ano você quer ver os episódios?");
        var ano =  in.nextInt();
        in.nextLine();

        LocalDate data = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(data))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " | Episódio: " + e.getTitulo() +
                                " | Data de Lançamento: " + e.getDataLancamento().format(formatador)
                ));
        System.out.println("---------------------------------------");

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);
        System.out.println("---------------------------------------");

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
    }
}
