package com.pinkroccade.assessment.somaroo.travis.person.person.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class PersonReadDto {

    private Long id;

    private String name;

    private Date birthDate;

    private PersonDto parent1;

    private PersonDto parent2;

    private PersonDto partner;

    private Set<Long> childIds;

}
