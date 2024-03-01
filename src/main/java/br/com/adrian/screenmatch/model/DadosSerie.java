package br.com.adrian.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //ignora os dados que não estão definidos no record
public record DadosSerie(
        @JsonAlias("Title") //define um ou mais apelidos para o nome da propriedade JSON associada ao campo Java.
        String titulo,

        @JsonAlias("totalSeasons")
        Integer totalTemporadas,

        @JsonAlias("imdbRating")
        String avaliacao

//        @JsonProperty("imdbVotes") define o nome da propriedade JSON que está associada ao campo Java.
//        String votos
) {
}
