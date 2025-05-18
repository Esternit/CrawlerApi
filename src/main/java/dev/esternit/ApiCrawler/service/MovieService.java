package dev.esternit.ApiCrawler.service;

import dev.esternit.ApiCrawler.dto.MovieDto;
import dev.esternit.ApiCrawler.dto.MoviePreviewDto;
import dev.esternit.ApiCrawler.mapper.MovieMapper;
import dev.esternit.ApiCrawler.repository.MovieRepository;
import dev.esternit.jooq.generated.tables.records.MovieRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    //Возвращает информацию о фильме с его актерами
    public MovieDto getMovieWithCast(Integer movieId) {
        var records = movieRepository.fetchMovieWithCastById(movieId);
        return movieMapper.mapToMovieDto(records);
    }

    //Возвращает информацию о фильмах
    public List<MoviePreviewDto> getMoviePreviews() {
        List<MovieRecord> records = movieRepository.findAll();
        return records.stream().map(movieMapper::mapToMoviePreviewDto).toList();
    }

    //Обновляет информацию о фильме
    @Transactional
    public Optional<MovieDto> updateMovie(Integer movieId, String title, LocalDate releaseDate,
            String imdbUrl, String type, String country, String description) {
        MovieRecord movie = movieRepository.update(movieId, title, releaseDate, imdbUrl, type, country, description);
        if (movie == null) {
            return Optional.empty();
        }

        var records = movieRepository.fetchMovieWithCastById(movieId);
        return Optional.ofNullable(movieMapper.mapToMovieDto(records));
    }

    //Удаляет информацию о фильме
    @Transactional
    public void deleteMovie(Integer movieId) {
        movieRepository.delete(movieId);
    }

}
