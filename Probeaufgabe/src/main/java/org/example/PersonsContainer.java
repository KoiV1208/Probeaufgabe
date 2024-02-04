package org.example;
import java.util.List;

public class PersonsContainer {

    private List<Person> persons;

    public PersonsContainer() {
    }

    public PersonsContainer(List<Person> persons) {
        this.persons = persons;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}