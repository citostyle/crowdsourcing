
package tuwien.aic.crowdsourcing.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Worker implements Serializable {

    private static final long serialVersionUID = -2356181318350122835L;

    private long id = -1L;
    private String workerId = "";
    private String country = "";
    private String city = "";
    private Gender gender = Gender.UNKNOWN;
    private int age = -1;
    private int outOfIQRCount = 0;

    public Worker() {

    }

    public Worker(String workerId) {
        this.workerId = workerId;
    }

    public Worker(String workerId, String country, String city, Gender gender,
            int age) {

        this.age = age;
        this.city = city;
        this.gender = gender;
        this.country = country;
        this.workerId = workerId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(unique = true, nullable = false)
    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    @Column(nullable = true)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(nullable = true)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(nullable = true)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Column(nullable = true)
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Column(nullable = false)
    public int getOutOfIQRCount() {
        return outOfIQRCount;
    }

    public void setOutOfIQRCount(int outOfIQRCount) {
        this.outOfIQRCount = outOfIQRCount;
    }
}
