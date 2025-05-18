package dev.esternit.ApiCrawler.repository;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import dev.esternit.generated.tables.MovieCast;
import dev.esternit.generated.tables.Person;
import dev.esternit.generated.tables.records.PersonRecord;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PersonRepository {

    private final DSLContext dsl;

    public List<PersonRecord> findAll() {
        return dsl.selectFrom(Person.PERSON)
                .orderBy(Person.PERSON.PERSON_ID)
                .fetch();
    }

    public Optional<PersonRecord> findById(Integer personId) {
        return dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.PERSON_ID.eq(personId))
                .fetchOptional();
    }

    public PersonRecord create(String fullName, String imdbId) {
        return dsl.insertInto(Person.PERSON)
                .set(Person.PERSON.FULL_NAME, fullName)
                .set(Person.PERSON.IMDB_ID, imdbId)
                .returning()
                .fetchOne();
    }

    public PersonRecord update(Integer personId, String fullName, String imdbId) {
        return dsl.update(Person.PERSON)
                .set(Person.PERSON.FULL_NAME, fullName)
                .set(Person.PERSON.IMDB_ID, imdbId)
                .where(Person.PERSON.PERSON_ID.eq(personId))
                .returning()
                .fetchOne();
    }

    public void delete(Integer personId) {
        dsl.deleteFrom(Person.PERSON)
                .where(Person.PERSON.PERSON_ID.eq(personId))
                .execute();
    }

    public void addPersonToMovie(Integer personId, Integer movieId, String role) {
        dsl.insertInto(MovieCast.MOVIE_CAST)
                .set(MovieCast.MOVIE_CAST.PERSON_ID, personId)
                .set(MovieCast.MOVIE_CAST.MOVIE_ID, movieId)
                .set(MovieCast.MOVIE_CAST.ROLE, role)
                .execute();
    }

    public void removePersonFromMovie(Integer personId, Integer movieId) {
        dsl.deleteFrom(MovieCast.MOVIE_CAST)
                .where(MovieCast.MOVIE_CAST.PERSON_ID.eq(personId))
                .and(MovieCast.MOVIE_CAST.MOVIE_ID.eq(movieId))
                .execute();
    }
}
