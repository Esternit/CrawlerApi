package dev.esternit.ApiCrawler.repository;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import dev.esternit.jooq.generated.tables.MovieCast;
import dev.esternit.jooq.generated.tables.Person;
import dev.esternit.jooq.generated.tables.records.PersonRecord;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PersonRepository {

    private final DSLContext dsl;

    /**
     * SELECT * FROM person ORDER BY person_id;
     */
    public List<PersonRecord> findAll() {
        return dsl.selectFrom(Person.PERSON)
                .orderBy(Person.PERSON.PERSON_ID)
                .fetch();
    }

    /**
     * SELECT * FROM person WHERE person_id = ?;
     */
    public Optional<PersonRecord> findById(Integer personId) {
        return dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.PERSON_ID.eq(personId))
                .fetchOptional();
    }

    /**
     * INSERT INTO person (full_name, imdb_id) VALUES (?, ?) RETURNING *;
     */
    public PersonRecord create(String fullName, String imdbId) {
        return dsl.insertInto(Person.PERSON)
                .set(Person.PERSON.FULL_NAME, fullName)
                .set(Person.PERSON.IMDB_ID, imdbId)
                .returning()
                .fetchOne();
    }

    /**
     * UPDATE person SET full_name = ?, imdb_id = ? WHERE person_id = ? RETURNING *;
     */
    public PersonRecord update(Integer personId, String fullName, String imdbId) {
        return dsl.update(Person.PERSON)
                .set(Person.PERSON.FULL_NAME, fullName)
                .set(Person.PERSON.IMDB_ID, imdbId)
                .where(Person.PERSON.PERSON_ID.eq(personId))
                .returning()
                .fetchOne();
    }

    /**
     * DELETE FROM person WHERE person_id = ?;
     */
    public void delete(Integer personId) {
        dsl.deleteFrom(Person.PERSON)
                .where(Person.PERSON.PERSON_ID.eq(personId))
                .execute();
    }

    /**
     * INSERT INTO movie_cast (movie_id, person_id, role) VALUES (?, ?, ?);
     */
    public void addPersonToMovie(Integer personId, Integer movieId, String role) {
        dsl.insertInto(MovieCast.MOVIE_CAST)
                .set(MovieCast.MOVIE_CAST.PERSON_ID, personId)
                .set(MovieCast.MOVIE_CAST.MOVIE_ID, movieId)
                .set(MovieCast.MOVIE_CAST.ROLE, role)
                .execute();
    }

    /**
     * DELETE FROM movie_cast WHERE person_id = ? AND movie_id = ?;
     */
    public void removePersonFromMovie(Integer personId, Integer movieId) {
        dsl.deleteFrom(MovieCast.MOVIE_CAST)
                .where(MovieCast.MOVIE_CAST.PERSON_ID.eq(personId))
                .and(MovieCast.MOVIE_CAST.MOVIE_ID.eq(movieId))
                .execute();
    }
}
