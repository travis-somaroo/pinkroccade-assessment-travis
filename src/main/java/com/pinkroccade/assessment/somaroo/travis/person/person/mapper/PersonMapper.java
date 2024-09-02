package com.pinkroccade.assessment.somaroo.travis.person.person.mapper;

import com.pinkroccade.assessment.somaroo.travis.person.person.data.PersonDto;
import com.pinkroccade.assessment.somaroo.travis.person.person.data.PersonReadDto;
import com.pinkroccade.assessment.somaroo.travis.person.person.model.Person;
import com.pinkroccade.assessment.somaroo.travis.person.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PersonMapper {

    private final PersonRepository personRepository;

    public Person toEntity(PersonDto dto) {
        Person person = new Person();
        person.setId(dto.getId());
        person.setName(dto.getName());
        person.setBirthDate(dto.getBirthDate());

        if (dto.getPartnerId() != null) {
            Optional<Person> partnerOpt = personRepository.findById(dto.getPartnerId());
            partnerOpt.ifPresent(person::linkPartner);
        }

        if (dto.getChildIds() != null) {
            for (Long childId : dto.getChildIds()) {
                Optional<Person> childOpt = personRepository.findById(childId);
                childOpt.ifPresent(child -> {
                    person.getChildren().add(child);
                    child.getChildren().add(person);
                });
            }
        }

        return person;
    }

    public PersonReadDto toDto(Person person) {
        PersonReadDto dto = new PersonReadDto();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setBirthDate(person.getBirthDate());

        if (person.getParent1() != null) {
            PersonDto parent1Dto = new PersonDto();
            parent1Dto.setId(person.getParent1().getId());
            dto.setParent1(parent1Dto);
        }

        if (person.getParent2() != null) {
            PersonDto parent2Dto = new PersonDto();
            parent2Dto.setId(person.getParent2().getId());
            dto.setParent2(parent2Dto);
        }

        if (person.getPartner() != null) {
            PersonDto partnerDto = new PersonDto();
            partnerDto.setId(person.getPartner().getId());
            dto.setPartner(partnerDto);
        }

        dto.setChildIds(person.getChildren().stream().map(Person::getId).collect(Collectors.toSet()));

        return dto;
    }

}
