package dev.esternit.ApiCrawler.controller;

import dev.esternit.ApiCrawler.dto.MovieDto;
import dev.esternit.ApiCrawler.dto.MoviePreviewDto;
import dev.esternit.ApiCrawler.dto.request.UpdateMovieRequest;
import dev.esternit.ApiCrawler.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/movie")
@Tag(name = "Movie API")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("")
    @Operation(summary = "Get all movies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All movies found"),})
    public ResponseEntity<List<MoviePreviewDto>> getMoviePreviews() {
        var all = movieService.getMoviePreviews();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{movieId}")
    @Operation(summary = "Get movie by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movie found"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<MovieDto> getMovieWithCast(@PathVariable Integer movieId) {
        var movie = movieService.getMovieWithCast(movieId);
        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PutMapping("/{movieId}")
    @Operation(summary = "Update an existing movie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movie updated successfully"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<MovieDto> updateMovie(
            @PathVariable Integer movieId,
            @RequestBody UpdateMovieRequest request) {
        return movieService.updateMovie(
                movieId,
                request.title(),
                request.releaseDate(),
                request.imdbUrl(),
                request.type(),
                request.country(),
                request.description()
        ).map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{movieId}")
    @Operation(summary = "Delete a movie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Movie deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer movieId) {
        movieService.deleteMovie(movieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
