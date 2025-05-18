package dev.esternit.ApiCrawler.service;

import dev.esternit.ApiCrawler.dto.PersonDto;
import dev.esternit.ApiCrawler.mapper.PersonMapper;
import dev.esternit.ApiCrawler.repository.PersonRepository;
import dev.esternit.jooq.generated.tables.records.PersonRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    private PersonRecord personRecord1;
    private PersonRecord personRecord2;
    private PersonDto personDto1;
    private PersonDto personDto2;

    @BeforeEach
    void setUp() {
        // Setup test data
        personRecord1 = new PersonRecord();
        personRecord1.setPersonId(1);
        personRecord1.setFullName("John Doe");
        personRecord1.setImdbId("nm1234567");

        personRecord2 = new PersonRecord();
        personRecord2.setPersonId(2);
        personRecord2.setFullName("Jane Smith");
        personRecord2.setImdbId("nm7654321");

        personDto1 = new PersonDto(1, "John Doe", "nm1234567");
        personDto2 = new PersonDto(2, "Jane Smith", "nm7654321");
    }

    @Test
    void getAllPersons_ShouldReturnAllPersons() {
        // Arrange
        when(personRepository.findAll()).thenReturn(Arrays.asList(personRecord1, personRecord2));
        when(personMapper.mapToDto(personRecord1)).thenReturn(personDto1);
        when(personMapper.mapToDto(personRecord2)).thenReturn(personDto2);

        // Act
        List<PersonDto> result = personService.getAllPersons();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(personDto1, result.get(0));
        assertEquals(personDto2, result.get(1));
        verify(personRepository).findAll();
        verify(personMapper, times(2)).mapToDto(any(PersonRecord.class));
    }

    @Test
    void getPersonById_WhenPersonExists_ShouldReturnPerson() {
        // Arrange
        when(personRepository.findById(1)).thenReturn(Optional.of(personRecord1));
        when(personMapper.mapToDto(personRecord1)).thenReturn(personDto1);

        // Act
        Optional<PersonDto> result = personService.getPersonById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(personDto1, result.get());
        verify(personRepository).findById(1);
        verify(personMapper).mapToDto(personRecord1);
    }

    @Test
    void getPersonById_WhenPersonDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(personRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<PersonDto> result = personService.getPersonById(999);

        // Assert
        assertTrue(result.isEmpty());
        verify(personRepository).findById(999);
        verify(personMapper, never()).mapToDto(any(PersonRecord.class));
    }

    @Test
    void createPerson_ShouldCreateAndReturnPerson() {
        // Arrange
        String fullName = "New Person";
        String imdbId = "nm9999999";
        when(personRepository.create(fullName, imdbId)).thenReturn(personRecord1);
        when(personMapper.mapToDto(personRecord1)).thenReturn(personDto1);

        // Act
        PersonDto result = personService.createPerson(fullName, imdbId);

        // Assert
        assertNotNull(result);
        assertEquals(personDto1, result);
        verify(personRepository).create(fullName, imdbId);
        verify(personMapper).mapToDto(personRecord1);
    }

    @Test
    void updatePerson_WhenPersonExists_ShouldUpdateAndReturnPerson() {
        // Arrange
        Integer personId = 1;
        String newFullName = "Updated Name";
        String newImdbId = "nm1111111";
        when(personRepository.update(personId, newFullName, newImdbId)).thenReturn(personRecord1);
        when(personMapper.mapToDto(personRecord1)).thenReturn(personDto1);

        // Act
        Optional<PersonDto> result = personService.updatePerson(personId, newFullName, newImdbId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(personDto1, result.get());
        verify(personRepository).update(personId, newFullName, newImdbId);
        verify(personMapper).mapToDto(personRecord1);
    }

    @Test
    void updatePerson_WhenPersonDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        Integer personId = 999;
        String newFullName = "Updated Name";
        String newImdbId = "nm1111111";
        when(personRepository.update(personId, newFullName, newImdbId)).thenReturn(null);

        // Act
        Optional<PersonDto> result = personService.updatePerson(personId, newFullName, newImdbId);

        // Assert
        assertTrue(result.isEmpty());
        verify(personRepository).update(personId, newFullName, newImdbId);
        verify(personMapper, never()).mapToDto(any(PersonRecord.class));
    }

    @Test
    void deletePerson_ShouldCallRepositoryDelete() {
        // Arrange
        Integer personId = 1;

        // Act
        personService.deletePerson(personId);

        // Assert
        verify(personRepository).delete(personId);
    }

    @Test
    void addPersonToMovie_ShouldCallRepositoryAddPersonToMovie() {
        // Arrange
        Integer personId = 1;
        Integer movieId = 1;
        String role = "Actor";

        // Act
        personService.addPersonToMovie(personId, movieId, role);

        // Assert
        verify(personRepository).addPersonToMovie(personId, movieId, role);
    }

    @Test
    void removePersonFromMovie_ShouldCallRepositoryRemovePersonFromMovie() {
        // Arrange
        Integer personId = 1;
        Integer movieId = 1;

        // Act
        personService.removePersonFromMovie(personId, movieId);

        // Assert
        verify(personRepository).removePersonFromMovie(personId, movieId);
    }
}
