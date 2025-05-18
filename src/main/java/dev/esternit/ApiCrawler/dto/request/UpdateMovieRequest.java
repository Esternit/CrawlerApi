package dev.esternit.ApiCrawler.dto.request;

import java.time.LocalDate;

public record UpdateMovieRequest(
        String title,
        LocalDate releaseDate,
        String imdbUrl,
        String type,
        String country,
        String description
        ) {

}
