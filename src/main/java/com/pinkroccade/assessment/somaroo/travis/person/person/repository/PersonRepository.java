package com.pinkroccade.assessment.somaroo.travis.person.person.repository;

import com.pinkroccade.assessment.somaroo.travis.person.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
