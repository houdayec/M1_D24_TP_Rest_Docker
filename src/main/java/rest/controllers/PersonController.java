package rest.controllers;

import rest.model.Person;

import java.util.HashMap;
import java.util.Map;

public class PersonController {

    private static PersonController instance;

    public Map<String, Person> personMap = new HashMap<>();

    public static PersonController getInstance(){
        if(instance == null){
            instance = new PersonController();
        }
        return instance;
    }

    public Person CreatePerson(String firstname, String lastname, String surname, int age){
        Person newPerson = new Person(firstname, lastname, surname, age);
        personMap.put(String.valueOf(newPerson.getId()), newPerson);
        return newPerson;
    }

    public void UpdatePerson(Person p){
        personMap.put(String.valueOf(p.getId()), p);
    }

}
