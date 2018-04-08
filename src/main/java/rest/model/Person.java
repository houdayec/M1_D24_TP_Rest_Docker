package rest.model;

import rest.database.DatabaseManager;
import rest.exceptions.PersistanceException;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Person implements Serializable, Entity{

    @XmlElement(name="id")
    private int id;
    @XmlElement(name="firstname")
    private String firstname;
    @XmlElement(name="lastname")
    private String lastname;
    @XmlElement(name="surname")
    private String surname;
    @XmlElement(name="age")
    private int age;

    private static int lastGeneratedId = 0;

    private static PreparedStatement findAll;
    private static PreparedStatement findById;

    static {
        try {
            Connection connection = DatabaseManager.getConnection();
            findAll = connection.prepareStatement("SELECT * FROM REST.PEOPLE");
            findById = connection.prepareStatement("select * FROM REST.PEOPLE WHERE ID=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Person(){

    }

    public Person(String firstname, String lastname, String surname, int age) {
        this.id = lastGeneratedId++;
        this.firstname = firstname;
        this.lastname = lastname;
        this.surname = surname;
        this.age = age;
    }

    public Person(int id, String firstname, String lastname, String surname, int age) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.surname = surname;
        this.age = age;
    }


    public int getId() {
        return id;
    }


    public String getFirstname() {
        return firstname;
    }


    public String getLastname() {
        return lastname;
    }


    public String getSurname() {
        return surname;
    }


    public int getAge() {
        return age;
    }


    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                '}';
    }

    /**
     * Method to retrieve all choices contained in a specific poll
     * @return list of all choices from the db
     * @throws PersistanceException
     */
    public static List<Person> findAll() throws PersistanceException {
        try{
            ResultSet resultSet = findAll.executeQuery();
            List<Person> lp = new ArrayList<>();
            while(resultSet.next()){
                System.out.println("start size " + lp.size());
                System.out.println(resultSet.getInt("ID"));
                Person p = new Person(resultSet.getInt("ID"), resultSet.getString("FIRSTNAME"), resultSet.getString("LASTNAME"), resultSet.getString("SURNAME"), resultSet.getInt("AGE"));
                lp.add(p);
                System.out.println("final size " + lp.size());
            }
            return lp;
        }catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

    public static Person findById(int id) throws PersistanceException{
        try{
            findById.setInt(1,id);
            ResultSet resultSet = findById.executeQuery();
            Person p;
            if(resultSet.next()){
                p = new Person(resultSet.getInt("ID"), resultSet.getString("FIRSTNAME"), resultSet.getString("LASTNAME"), resultSet.getString("SURNAME"), resultSet.getInt("AGE"));
                System.out.println("PERSON FOUND FOR ID : " + id + " " + p.toString());
            }else{
                throw new PersistanceException("Person " + id + "not found in db");
            }
            return p;
        }catch (SQLException e){
            throw new PersistanceException(e);
        }
    }

    @Override
    public int persist(Connection connection) throws PersistanceException {
        int returnedID;
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO rest.people(firstname,lastname,surname,age) VALUES ('" + firstname + "','" + lastname+ "','" + surname+ "','" + age + "')", Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                returnedID = resultSet.getInt(1);
                //setIdChoice(returnedID);
                System.out.println("creation of the person: " + this);
                return returnedID;
            } else {
                throw  new PersistanceException("The key of the choice could not be found.");
            }
        }catch (SQLException e){
            throw new PersistanceException(e);
        }
    }

    @Override
    public void merge(Connection connection) throws PersistanceException {
        try {
            String query = "UPDATE REST.PEOPLE SET AGE='"+ age +"', FIRSTNAME='"+ firstname +"', LASTNAME='" + lastname +"', SURNAME='" + surname + "' WHERE ID='"+id+"\'";
            System.out.println(query);
            connection.createStatement().executeUpdate(query);
            System.out.println("merge of the person : " + this);
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

    @Override
    public void update(Connection connection) throws PersistanceException {
        Person person = findById(this.id);
        this.firstname = person.firstname;
        this.age = person.age;
        this.lastname = person.lastname;
        this.surname = person.surname;
        System.out.println("Update of the person: " + this);
    }


    @Override
    public void remove(Connection connection) throws PersistanceException {
        try {
            connection.createStatement().executeUpdate("DELETE FROM rest.people WHERE id=\'" + id+ "\'");
            System.out.println("delete of the person: " + this);
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }
}
