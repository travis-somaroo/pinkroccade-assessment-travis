package com.pinkroccade.assessment.somaroo.travis.person.person.service;

import com.pinkroccade.assessment.somaroo.travis.person.person.data.PersonDto;
import com.pinkroccade.assessment.somaroo.travis.person.person.data.PersonReadDto;
import com.pinkroccade.assessment.somaroo.travis.person.person.mapper.PersonMapper;
import com.pinkroccade.assessment.somaroo.travis.person.person.model.Person;
import com.pinkroccade.assessment.somaroo.travis.person.person.repository.PersonRepository;
import com.pinkroccade.assessment.somaroo.travis.person.person.util.CsvUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    @Override
    public Person createPerson(PersonDto dto) {
        Person person = personMapper.toEntity(dto);
        return personRepository.save(person);
    }

    @Override
    public List<PersonReadDto> getAllPersons(String sortBy, String direction) {
        Set<String> sortFields = Set.of("id", "name");
        Set<String> directions = Set.of("asc", "desc");

        if (!sortFields.contains(sortBy)) {
            sortBy = "id";
        }

        if (!directions.contains(direction.toLowerCase())) {
            direction = "asc";
        }
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        List<Person> persons = personRepository.findAll(sort);

        return persons.stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Person updatePerson(PersonDto dto) {
        Optional<Person> personOpt = personRepository.findById(dto.getId());
        if (personOpt.isEmpty()) {
            throw new RuntimeException("person not found with id: " + dto.getId());
        }

        Person person = personOpt.get();
        person.setName(dto.getName());
        person.setBirthDate(dto.getBirthDate());

        if (dto.getPartnerId() != null) {
            Optional<Person> partnerOpt = personRepository.findById(dto.getPartnerId());
            partnerOpt.ifPresent(person::linkPartner);
        } else {
            person.setPartner(null);
        }
        if (dto.getChildIds() != null) {
            person.getChildren().clear();

            for (Long childId : dto.getChildIds()) {
                Optional<Person> childOpt = personRepository.findById(childId);
                childOpt.ifPresent(child -> {
                    person.getChildren().add(child);
                    child.getChildren().add(person);
                });
            }
        } else {
            person.getChildren().clear();
        }
        personRepository.save(person);
        return person;
    }

    @Override
    public void deletePerson(Long id) {
        Optional<Person> personOpt = personRepository.findById(id);
        if (personOpt.isEmpty()) {
            throw new RuntimeException("Person not found with id: " + id);
        }

        Person person = personOpt.get();

        if (person.getParent1() != null) {
            person.getParent1().getChildren().remove(person);
        }

        if (person.getParent2() != null) {
            person.getParent2().getChildren().remove(person);
        }

        for (Person child : person.getChildren()) {
            child.getChildren().remove(person);
        }

        if (person.getPartner() != null) {
            person.getPartner().setPartner(null);
        }

        personRepository.delete(person);
    }

    @Override
    public String getFilteredPersonsBase64Csv() throws IOException {
        List<Person> allPersons = personRepository.findAll();


        List<Person> filteredPersons = allPersons.stream()
                .filter(person -> person.getPartner() != null)
                .filter(person -> person.getChildren().size() >= 3 && person.getPartner().getChildren().size() >= 3)
                .filter(this::hasChildUnder18)
                .collect(Collectors.toList());

        String csvContent = CsvUtils.generateCsv(filteredPersons);
        return CsvUtils.encodeToBase64(csvContent);
    }

    private boolean hasChildUnder18(Person person) {
        return person.getChildren().stream()
                .anyMatch(child -> child.getBirthDate().after(new java.util.Date(System.currentTimeMillis() - 18L * 365 * 24 * 60 * 60 * 1000)));
    }

}


