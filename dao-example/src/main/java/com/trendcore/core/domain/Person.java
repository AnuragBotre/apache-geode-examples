package com.trendcore.core.domain;

import com.trendcore.core.lang.DateTimeUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.util.Calendar;
import java.util.Date;

@Region("People")
public class Person implements Identifiable<Long> {

    private Gender gender;

    @Id
    private Long id;

    private Long birthDate;

    private String firstName;
    private String lastName;

    public Person() {
    }

    public Person(final Long id) {
        this.id = id;
    }


    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public Date getBirthDateAsDate() {
        return DateTimeUtils.asDate(getBirthDateAsCalendar());
    }

    public boolean isBirthDateKnown() {
        return (birthDate != null);
    }

    public Calendar getBirthDateAsCalendar() {
        if (isBirthDateKnown()) {
            Calendar birthDate = Calendar.getInstance();
            birthDate.clear();
            birthDate.setTimeInMillis(getBirthDate());
            return birthDate;
        }
        return null;
    }

    public String getName() {
        return String.format("%1$s %2$s", getFirstName(), getLastName());
    }

    @Override
    public String toString() {
        return String.format("{ @type = %1$s, id = %2$d, firstName = %3$s, lastName = %4$s, birthDate = %5$s, gender = %6$s }",
                getClass().getName(), getId(), getFirstName(), getLastName(), DateTimeUtils.format(getBirthDateAsDate(),
                        DateTimeUtils.BIRTH_DATE_FORMAT), getGender());
    }
}
