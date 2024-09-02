package com.pinkroccade.assessment.somaroo.travis.person.bootstrap;

import com.pinkroccade.assessment.somaroo.travis.person.person.model.Person;
import com.pinkroccade.assessment.somaroo.travis.person.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DummyDataRunner implements CommandLineRunner {

    private final PersonRepository personRepository;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        Person sue = new Person();
        sue.setName("Sue");
        sue.setBirthDate(new Date(82, Calendar.FEBRUARY, 1));
        sue.setPartner(null);

        Person savedSue = personRepository.save(sue);

        Person bob = new Person();
        bob.setName("Bob");
        bob.setBirthDate(new Date(80, Calendar.JANUARY, 1));
        bob.linkPartner(savedSue);

        Person savedBob = personRepository.save(bob);

        Person child1 = bob.createChild("Alice", new Date(2005, Calendar.JANUARY, 1), savedSue);
        Person child2 = bob.createChild("Bob Jr.", new Date(2007, Calendar.JUNE, 1), savedSue);
        Person child3 = bob.createChild("Charlie", new Date(2010, Calendar.MARCH, 1), savedSue);

        personRepository.save(child1);
        personRepository.save(child2);
        personRepository.save(child3);

        savedBob.getChildren().add(child1);
        savedBob.getChildren().add(child2);
        savedBob.getChildren().add(child3);
        savedSue.getChildren().add(child1);
        savedSue.getChildren().add(child2);
        savedSue.getChildren().add(child3);

        personRepository.save(savedBob);
        personRepository.save(savedSue);
    }

}
