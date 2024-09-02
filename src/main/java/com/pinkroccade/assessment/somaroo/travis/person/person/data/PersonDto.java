package com.pinkroccade.assessment.somaroo.travis.person.person.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class PersonDto {

    private Long id;

    private String name;

    private Date birthDate;

    private Long partnerId;

    private Set<Long> childIds;

}
