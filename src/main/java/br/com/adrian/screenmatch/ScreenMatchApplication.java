package br.com.adrian.screenmatch;

import br.com.adrian.screenmatch.model.DadosEpisodio;
import br.com.adrian.screenmatch.model.DadosSerie;
import br.com.adrian.screenmatch.service.ConsumoAPI;
import br.com.adrian.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenMatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var consumoAPI = new ConsumoAPI();
        var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=c8d98969");
        System.out.println(json);
        System.out.println("---------------------------------------");

//        json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
//        System.out.println(json);

        ConverteDados conversor = new ConverteDados();
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
        System.out.println("---------------------------------------");

        json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=c8d98969");
        DadosEpisodio episodio = conversor.obterDados(json, DadosEpisodio.class);
        System.out.println(episodio);
    }
}
