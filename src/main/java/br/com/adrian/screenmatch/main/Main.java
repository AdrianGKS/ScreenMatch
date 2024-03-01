package br.com.adrian.screenmatch.main;

import br.com.adrian.screenmatch.model.DadosEpisodio;
import br.com.adrian.screenmatch.model.DadosSerie;
import br.com.adrian.screenmatch.model.DadosTemporada;
import br.com.adrian.screenmatch.service.ConsumoAPI;
import br.com.adrian.screenmatch.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private Scanner in = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
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
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }

}
