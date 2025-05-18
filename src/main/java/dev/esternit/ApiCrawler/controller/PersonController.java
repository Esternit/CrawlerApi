package dev.esternit.ApiCrawler.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.esternit.ApiCrawler.dto.PersonDto;
import dev.esternit.ApiCrawler.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/person")
@Tag(name = "Person API")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("")
    @Operation(summary = "Get all persons")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All persons found"),})
    public ResponseEntity<List<PersonDto>> getAllPersons() {
        return new ResponseEntity<>(personService.getAllPersons(), HttpStatus.OK);
    }

    @GetMapping("/{personId}")
    @Operation(summary = "Get person by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Person found"),
        @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<PersonDto> getPersonById(@PathVariable Integer personId) {
        return personService.getPersonById(personId)
                .map(person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    @Operation(summary = "Create a new person")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Person created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<PersonDto> createPerson(
            @RequestParam String fullName,
            @RequestParam String imdbId) {
        PersonDto person = personService.createPerson(fullName, imdbId);
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/{personId}")
    @Operation(summary = "Update an existing person")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Person updated successfully"),
        @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<PersonDto> updatePerson(
            @PathVariable Integer personId,
            @RequestParam String fullName,
            @RequestParam String imdbId) {
        return personService.updatePerson(personId, fullName, imdbId)
                .map(person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{personId}")
    @Operation(summary = "Delete a person")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<Void> deletePerson(@PathVariable Integer personId) {
        personService.deletePerson(personId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{personId}/movie/{movieId}")
    @Operation(summary = "Add a person to a movie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Person added to movie successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Void> addPersonToMovie(
            @PathVariable Integer personId,
            @PathVariable Integer movieId,
            @RequestParam String role) {
        personService.addPersonToMovie(personId, movieId, role);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{personId}/movie/{movieId}")
    @Operation(summary = "Remove a person from a movie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Person removed from movie successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Void> removePersonFromMovie(
            @PathVariable Integer personId,
            @PathVariable Integer movieId) {
        personService.removePersonFromMovie(personId, movieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
