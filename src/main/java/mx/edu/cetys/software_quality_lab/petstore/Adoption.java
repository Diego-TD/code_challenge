package mx.edu.cetys.software_quality_lab.petstore;

import jakarta.persistence.*;
import mx.edu.cetys.software_quality_lab.pets.Pet;
import mx.edu.cetys.software_quality_lab.users.User;

import java.time.LocalDate;

@Entity
@Table(name = "petstore_adoptions")
public class Adoption {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Referencia al Pet del módulo pets/ — petstore reutiliza el catálogo genérico
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus status = AdoptionStatus.ACTIVE;

    private LocalDate adoptionDate;

    public Adoption() {}

    public Adoption(User user, Pet pet) {
        this.user = user;
        this.pet = pet;
        this.status = AdoptionStatus.ACTIVE;
        this.adoptionDate = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }

    public AdoptionStatus getStatus() { return status; }
    public void setStatus(AdoptionStatus status) { this.status = status; }

    public LocalDate getAdoptionDate() { return adoptionDate; }
    public void setAdoptionDate(LocalDate adoptionDate) { this.adoptionDate = adoptionDate; }
}
