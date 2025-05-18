package dev.esternit.ApiCrawler.service;

import dev.esternit.ApiCrawler.dto.MovieDto;
import dev.esternit.ApiCrawler.dto.MoviePreviewDto;
import dev.esternit.ApiCrawler.mapper.MovieMapper;
import dev.esternit.ApiCrawler.repository.MovieRepository;
import dev.esternit.jooq.generated.tables.records.MovieRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public MovieDto getMovieWithCast(Integer movieId) {
        var records = movieRepository.fetchMovieWithCastById(movieId);
        return movieMapper.mapToMovieDto(records);
    }

    public List<MoviePreviewDto> getMoviePreviews() {
        List<MovieRecord> records = movieRepository.findAll();
        return records.stream().map(movieMapper::mapToMoviePreviewDto).toList();
    }

}
