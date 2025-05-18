package dev.esternit.ApiCrawler.mapper;

import dev.esternit.ApiCrawler.dto.CastDto;
import dev.esternit.ApiCrawler.dto.MovieDto;
import dev.esternit.ApiCrawler.dto.MoviePreviewDto;
import dev.esternit.ApiCrawler.dto.PersonDto;
import dev.esternit.jooq.generated.tables.Movie;
import dev.esternit.jooq.generated.tables.MovieCast;
import dev.esternit.jooq.generated.tables.Person;
import dev.esternit.jooq.generated.tables.records.MovieRecord;
import dev.esternit.jooq.generated.tables.records.PersonRecord;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieMapper {

    public MovieDto mapToMovieDto(List<Record> records) {
        if (records.isEmpty()) return null;

        MovieRecord movie = records.getFirst().into(Movie.MOVIE);

        List<CastDto> cast = records.stream()
                .filter(r -> r.get(Person.PERSON.PERSON_ID) != null)
                .map(r -> {
                    PersonRecord person = r.into(Person.PERSON);
                    String role = r.get(MovieCast.MOVIE_CAST.ROLE);
                    return new CastDto(
                            new PersonDto(
                                    person.getPersonId(),
                                    person.getFullName(),
                                    person.getImdbId()
                            ),
                            role
                    );
                })
                .toList();

        return new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getImdbUrl(),
                movie.getType(),
                movie.getCountry(),
                movie.getDescription(),
                cast
        );
    }

    public MoviePreviewDto mapToMoviePreviewDto(MovieRecord movie) {
        if (movie == null) return null;

        return new MoviePreviewDto(
                movie.getMovieId(),
                movie.getTitle()
        );
    }
}
