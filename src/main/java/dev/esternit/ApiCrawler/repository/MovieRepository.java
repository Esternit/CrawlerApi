package dev.esternit.ApiCrawler.repository;

import dev.esternit.jooq.generated.tables.Movie;
import dev.esternit.jooq.generated.tables.MovieCast;
import dev.esternit.jooq.generated.tables.Person;
import dev.esternit.jooq.generated.tables.records.MovieRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieRepository {

    private final DSLContext dsl;

    public List<MovieRecord> findAll() {
        return dsl.selectFrom(Movie.MOVIE).orderBy(Movie.MOVIE.MOVIE_ID).fetch();
    }

    public List<Record> fetchMovieWithCastById(Integer movieId) {
        return dsl.select()
                .from(Movie.MOVIE)
                .leftJoin(MovieCast.MOVIE_CAST).on(MovieCast.MOVIE_CAST.MOVIE_ID.eq(Movie.MOVIE.MOVIE_ID))
                .leftJoin(Person.PERSON).on(Person.PERSON.PERSON_ID.eq(MovieCast.MOVIE_CAST.PERSON_ID))
                .where(Movie.MOVIE.MOVIE_ID.eq(movieId))
                .fetch();
    }

    public MovieRecord update(Integer movieId, String title, LocalDate releaseDate, String imdbUrl,
            String type, String country, String description) {
        return dsl.update(Movie.MOVIE)
                .set(Movie.MOVIE.TITLE, title)
                .set(Movie.MOVIE.RELEASE_DATE, releaseDate)
                .set(Movie.MOVIE.IMDB_URL, imdbUrl)
                .set(Movie.MOVIE.TYPE, type)
                .set(Movie.MOVIE.COUNTRY, country)
                .set(Movie.MOVIE.DESCRIPTION, description)
                .set(Movie.MOVIE.UPDATED_AT, OffsetDateTime.now())
                .where(Movie.MOVIE.MOVIE_ID.eq(movieId))
                .returning()
                .fetchOne();
    }

    public void delete(Integer movieId) {
        dsl.deleteFrom(Movie.MOVIE)
                .where(Movie.MOVIE.MOVIE_ID.eq(movieId))
                .execute();
    }
}
