package dev.esternit.ApiCrawler.mapper;

import org.springframework.stereotype.Component;

import dev.esternit.ApiCrawler.dto.PersonDto;
import dev.esternit.generated.tables.records.PersonRecord;

@Component
public class PersonMapper {

    public PersonDto mapToDto(PersonRecord person) {
        if (person == null) {
            return null;
        }

        return new PersonDto(
                person.getPersonId(),
                person.getFullName(),
                person.getImdbId()
        );
    }
}
