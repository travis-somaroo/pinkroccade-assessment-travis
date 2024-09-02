package com.pinkroccade.assessment.somaroo.travis.person.person.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date birthDate;

    @ManyToOne
    @JoinColumn(name = "parent1_id")
    @JsonIgnore
    private Person parent1;

    @ManyToOne
    @JoinColumn(name = "parent2_id")
    @JsonIgnore
    private Person parent2;

    @ManyToMany
    @JoinTable(
            name = "person_children",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    @JsonIgnore
    private Set<Person> children = new HashSet<>();

    @OneToOne()
    @JoinColumn(name = "partner_id")
    @JsonIgnore
    private Person partner;

    /**
     * Creates the child and link to parent 1 and parent 2.
     */
    public Person createChild(String name, Date birthDate, Person parent2) {
        Person child = new Person();
        child.setName(name);
        child.setBirthDate(birthDate);
        this.children.add(child);
        child.setParent1(this);

        if (parent2 != null) {
            parent2.getChildren().add(child);
            child.setParent2(parent2);
        }
        return child;
    }

    /**
     * Links this object to a partner if provided.
     */
    public void linkPartner(Person partner) {
        this.partner = partner;
        if (partner.getPartner() != this) {
            partner.setPartner(this);
        }
    }

}
