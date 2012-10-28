package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Worker implements Serializable {

    private Long id = -1L;

    private String workerId = "";
    
    private String country = "";
    
    private String city = "";
    
    private char gender = 'u';
    
    private int age = -1;

    public Worker() {
        
    }
    
    public Worker(Long id, String workerId) {
        this.id = id;
        
        this.workerId = workerId;
    }
    
    public Worker(Long id, String workerId, 
                  String country, 
                  String city,
                  char gender, 
                  int age) {
        
        this.id = id;
        this.age = age;
        this.city = city;
        this.gender = gender;
        this.country = country;
        this.workerId = workerId;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
