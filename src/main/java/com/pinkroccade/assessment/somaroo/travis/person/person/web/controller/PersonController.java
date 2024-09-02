package com.pinkroccade.assessment.somaroo.travis.person.person.web.controller;

import com.pinkroccade.assessment.somaroo.travis.person.person.data.PersonDto;
import com.pinkroccade.assessment.somaroo.travis.person.person.data.PersonReadDto;
import com.pinkroccade.assessment.somaroo.travis.person.person.model.Person;
import com.pinkroccade.assessment.somaroo.travis.person.person.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody PersonDto dto) {
        Person createdPerson = personService.createPerson(dto);
        return ResponseEntity.ok(createdPerson);
    }

    @GetMapping
    public ResponseEntity<List<PersonReadDto>> getAllPersons(
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        List<PersonReadDto> persons = personService.getAllPersons(sortBy, direction);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping("/filtered-csv")
    public ResponseEntity<byte[]> getFilteredPersonsCsv() {
        try {
            String csvContent = personService.getFilteredPersonsBase64Csv();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=persons.csv");
            byte[] csvBytes = csvContent.getBytes();
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(("error generating csv").getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody PersonDto personDto) {
        personDto.setId(id);
        Person updatedPerson = personService.updatePerson(personDto);
        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
