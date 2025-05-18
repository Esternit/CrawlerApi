package dev.esternit.ApiCrawler.service;

import dev.esternit.ApiCrawler.dto.CastDto;
import dev.esternit.ApiCrawler.dto.MovieDto;
import dev.esternit.ApiCrawler.dto.MoviePreviewDto;
import dev.esternit.ApiCrawler.dto.PersonDto;
import dev.esternit.ApiCrawler.mapper.MovieMapper;
import dev.esternit.ApiCrawler.repository.MovieRepository;
import dev.esternit.generated.tables.records.MovieRecord;
import org.jooq.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;

    private MovieRecord movieRecord;
    private MovieDto movieDto;
    private MoviePreviewDto moviePreviewDto;
    private List<Record> movieWithCastRecords;

    @BeforeEach
    void setUp() {
        // Setup test data
        movieRecord = new MovieRecord();
        movieRecord.setMovieId(1);
        movieRecord.setTitle("Test Movie");
        movieRecord.setReleaseDate(LocalDate.now());
        movieRecord.setImdbUrl("https://imdb.com/test");
        movieRecord.setType("movie");
        movieRecord.setCountry("USA");
        movieRecord.setDescription("Test description");

        movieDto = new MovieDto(
                1,
                "Test Movie",
                LocalDate.now(),
                "https://imdb.com/test",
                "movie",
                "USA",
                "Test description",
                Arrays.asList(new CastDto(
                        new PersonDto(1, "John Doe", "nm1234567"),
                        "Actor"
                ))
        );

        moviePreviewDto = new MoviePreviewDto(
                1,
                "Test Movie"
        );

        movieWithCastRecords = Arrays.asList(mock(Record.class));
    }

    @Test
    void getMovieWithCast_WhenMovieExists_ShouldReturnMovie() {
        // Arrange
        when(movieRepository.fetchMovieWithCastById(1)).thenReturn(movieWithCastRecords);
        when(movieMapper.mapToMovieDto(movieWithCastRecords)).thenReturn(movieDto);

        // Act
        MovieDto result = movieService.getMovieWithCast(1);

        // Assert
        assertNotNull(result);
        assertEquals(movieDto, result);
        verify(movieRepository).fetchMovieWithCastById(1);
        verify(movieMapper).mapToMovieDto(movieWithCastRecords);
    }

    @Test
    void getMovieWithCast_WhenMovieDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(movieRepository.fetchMovieWithCastById(999)).thenReturn(List.of());
        when(movieMapper.mapToMovieDto(List.of())).thenReturn(null);

        // Act
        MovieDto result = movieService.getMovieWithCast(999);

        // Assert
        assertNull(result);
        verify(movieRepository).fetchMovieWithCastById(999);
        verify(movieMapper).mapToMovieDto(List.of());
    }

    @Test
    void getMoviePreviews_ShouldReturnAllMoviePreviews() {
        // Arrange
        List<MovieRecord> records = Arrays.asList(movieRecord);
        when(movieRepository.findAll()).thenReturn(records);
        when(movieMapper.mapToMoviePreviewDto(movieRecord)).thenReturn(moviePreviewDto);

        // Act
        List<MoviePreviewDto> result = movieService.getMoviePreviews();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(moviePreviewDto, result.get(0));
        verify(movieRepository).findAll();
        verify(movieMapper).mapToMoviePreviewDto(movieRecord);
    }

    @Test
    void updateMovie_WhenMovieExists_ShouldUpdateAndReturnMovie() {
        // Arrange
        Integer movieId = 1;
        String title = "Updated Movie";
        LocalDate releaseDate = LocalDate.now();
        String imdbUrl = "https://imdb.com/updated";
        String type = "movie";
        String country = "UK";
        String description = "Updated description";

        when(movieRepository.update(movieId, title, releaseDate, imdbUrl, type, country, description))
                .thenReturn(movieRecord);
        when(movieRepository.fetchMovieWithCastById(movieId)).thenReturn(movieWithCastRecords);
        when(movieMapper.mapToMovieDto(movieWithCastRecords)).thenReturn(movieDto);

        // Act
        Optional<MovieDto> result = movieService.updateMovie(movieId, title, releaseDate, imdbUrl, type, country, description);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(movieDto, result.get());
        verify(movieRepository).update(movieId, title, releaseDate, imdbUrl, type, country, description);
        verify(movieRepository).fetchMovieWithCastById(movieId);
        verify(movieMapper).mapToMovieDto(movieWithCastRecords);
    }

    @Test
    void updateMovie_WhenMovieDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        Integer movieId = 999;
        String title = "Updated Movie";
        LocalDate releaseDate = LocalDate.now();
        String imdbUrl = "https://imdb.com/updated";
        String type = "movie";
        String country = "UK";
        String description = "Updated description";

        when(movieRepository.update(movieId, title, releaseDate, imdbUrl, type, country, description))
                .thenReturn(null);

        // Act
        Optional<MovieDto> result = movieService.updateMovie(movieId, title, releaseDate, imdbUrl, type, country, description);

        // Assert
        assertTrue(result.isEmpty());
        verify(movieRepository).update(movieId, title, releaseDate, imdbUrl, type, country, description);
        verify(movieRepository, never()).fetchMovieWithCastById(any());
        verify(movieMapper, never()).mapToMovieDto(any());
    }

    @Test
    void deleteMovie_ShouldCallRepositoryDelete() {
        // Arrange
        Integer movieId = 1;

        // Act
        movieService.deleteMovie(movieId);

        // Assert
        verify(movieRepository).delete(movieId);
    }
}
