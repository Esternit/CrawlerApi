package dev.esternit.ApiCrawler.dto;

import java.time.LocalDate;
import java.util.List;

public record MovieDto(
        Integer movieId,
        String title,
        LocalDate releaseDate,
        String imdbUrl,
        String type,
        String country,
        String description,
        List<CastDto> cast
) {}
