package dev.esternit.ApiCrawler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.esternit.ApiCrawler.dto.PersonDto;
import dev.esternit.ApiCrawler.mapper.PersonMapper;
import dev.esternit.ApiCrawler.repository.PersonRepository;
import dev.esternit.jooq.generated.tables.records.PersonRecord;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    //Выводит всех актёров
    public List<PersonDto> getAllPersons() {
        return personRepository.findAll().stream()
                .map(personMapper::mapToDto)
                .toList();
    }

    //Выводит актера по id
    public Optional<PersonDto> getPersonById(Integer personId) {
        return personRepository.findById(personId)
                .map(personMapper::mapToDto);
    }

    //Создает актера
    @Transactional
    public PersonDto createPerson(String fullName, String imdbId) {
        PersonRecord person = personRepository.create(fullName, imdbId);
        return personMapper.mapToDto(person);
    }

    //Обновляет актера
    @Transactional
    public Optional<PersonDto> updatePerson(Integer personId, String fullName, String imdbId) {
        return Optional.ofNullable(personRepository.update(personId, fullName, imdbId))
                .map(personMapper::mapToDto);
    }

    //Удаляет актера
    @Transactional
    public void deletePerson(Integer personId) {
        personRepository.delete(personId);
    }

    //Добавляет актера в фильм
    @Transactional
    public void addPersonToMovie(Integer personId, Integer movieId, String role) {
        personRepository.addPersonToMovie(personId, movieId, role);
    }

    //Удаляет актера из фильма
    @Transactional
    public void removePersonFromMovie(Integer personId, Integer movieId) {
        personRepository.removePersonFromMovie(personId, movieId);
    }
}
