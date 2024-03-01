package br.com.adrian.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosSerie(
        @JsonAlias("title") //define um ou mais apelidos para o nome da propriedade JSON associada ao campo Java.
        String titulo,

        @JsonAlias("totalSeasons")
        Integer totalTemporadas,

        @JsonAlias("imdbRating")
        String avaliacao

//        @JsonProperty("imdbVotes") define o nome da propriedade JSON que está associada ao campo Java.
//        String votos
) {
}
