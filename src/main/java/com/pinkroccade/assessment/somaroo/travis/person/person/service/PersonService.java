package com.pinkroccade.assessment.somaroo.travis.person.person.service;

import com.pinkroccade.assessment.somaroo.travis.person.person.data.PersonDto;
import com.pinkroccade.assessment.somaroo.travis.person.person.data.PersonReadDto;
import com.pinkroccade.assessment.somaroo.travis.person.person.model.Person;

import java.io.IOException;
import java.util.List;

public interface PersonService {

    Person createPerson(PersonDto dto);

    List<PersonReadDto> getAllPersons(String sortBy, String direction);

    Person updatePerson(PersonDto dto);

    void deletePerson(Long id);

    String getFilteredPersonsBase64Csv() throws IOException;

}
