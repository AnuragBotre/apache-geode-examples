package com.trendcore.cache.springboot;

import com.trendcore.core.domain.Person;
import com.trendcore.core.lang.IdentifierSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DaoCommandLineRunner implements CommandLineRunner {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(DaoCommandLineRunner.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    private Person jonDoe;
    private Person janeDoe;
    private Person cookieDoe;
    private Person froDoe;
    private Person pieDoe;
    private Person sourDoe;
    private Person jackBlack;
    private Person jackHandy;
    private Person sandyHandy;

    @Autowired
    private PersonRepository personRepository;


    protected Person createPerson(final String firstName, final String lastName) {
        Person person = new Person(firstName, lastName);
        IdentifierSequence.INSTANCE.setSequentialLongId(person);
        return person;
    }


    protected <T> T log(T object, String message) {
        System.err.printf("%1$s (%2$s)%n", message, object);
        return object;
    }

    protected Person put(final Person person) {
        return personRepository.save(person);
    }


    protected void setupPeople() {
        jonDoe = put(createPerson("Jon", "Doe"));
        janeDoe = put(createPerson("Jane", "Doe"));
        cookieDoe = put(createPerson("Cookie", "Doe"));
        froDoe = put(createPerson("Fro", "Doe"));
        pieDoe = put(createPerson("Pie", "Doe"));
        sourDoe = put(createPerson("Sour", "Doe"));
        jackBlack = put(createPerson("Jack", "Black"));
        jackHandy = put(createPerson("Jack", "Handy"));
        sandyHandy = put(createPerson("Sandy", "Handy"));
    }


    protected List<String> toNames(List<Person> people) {
        List<String> names = new ArrayList<>(people.size());

        for (Person person : people) {
            names.add(person.getName());
        }

        return names;
    }


    @Override
    public void run(String... args) throws Exception {
        setupPeople();


        long peopleCount = log(personRepository.count(), "Number of people");

        Assert.isTrue(peopleCount == 9, String.format("Expected 9; but was (%1$d)", peopleCount));

        List<Person> actualDoeFamily = personRepository.findByLastName("Doe");
        List<Person> expectedDoeFamily = Arrays.asList(jonDoe, janeDoe, cookieDoe, froDoe, pieDoe, sourDoe);

        log(toNames(actualDoeFamily), "Actual 'Doe' family is");

        Assert.notNull(actualDoeFamily, "Doe family must not be null");
        Assert.isTrue(actualDoeFamily.containsAll(expectedDoeFamily), String.format("Expected (%1$s); but was (%2$s)",
                expectedDoeFamily, actualDoeFamily));
        Assert.isTrue(actualDoeFamily.size() == expectedDoeFamily.size(), String.format(
                "Expected (%1$d); but was (%2$d); additional people include (%3$s)",
                expectedDoeFamily.size(), actualDoeFamily.size(), actualDoeFamily.removeAll(expectedDoeFamily)));
    }
}
