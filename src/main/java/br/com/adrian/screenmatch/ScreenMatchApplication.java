package br.com.adrian.screenmatch;

import br.com.adrian.screenmatch.service.ConsumoAPI;
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
//        json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
//        System.out.println(json);
    }
}
