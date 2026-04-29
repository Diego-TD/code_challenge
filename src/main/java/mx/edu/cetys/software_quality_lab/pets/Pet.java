package mx.edu.cetys.software_quality_lab.pets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="pets")
public class Pet {

    // This is a classic POJO (Plain Old Java Object)

    @Id
    @GeneratedValue()
    private Long id;

    private String name;
    private String race;
    private String color;
    private Integer age;
    // Indica si el pet está disponible para adopción en el petstore
    private boolean available = false;

    public Pet() {}
    public Pet(String name, String race, String color, Integer age) {
        this.name = name;
        this.race = race;
        this.color = color;
        this.age = age;
        this.available = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
