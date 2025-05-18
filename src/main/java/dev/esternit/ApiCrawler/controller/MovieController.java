package dev.esternit.ApiCrawler.controller;

import dev.esternit.ApiCrawler.dto.MovieDto;
import dev.esternit.ApiCrawler.dto.MoviePreviewDto;
import dev.esternit.ApiCrawler.service.MovieService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/movie")
@Tag(name = "Movie API")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All movies found"),
    })
    public ResponseEntity<List<MoviePreviewDto>> getMoviePreviews() {
        var all = movieService.getMoviePreviews();
        return new ResponseEntity<>(all,HttpStatus.OK);
    }

    @GetMapping("/{movieId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie found"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<MovieDto> getMovieWithCast(@PathVariable Integer movieId) {
        if (movieId == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        var movie = movieService.getMovieWithCast(movieId);
        if (movie == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(movie,HttpStatus.OK);
    }
}
