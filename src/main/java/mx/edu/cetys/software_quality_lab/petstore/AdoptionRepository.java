package mx.edu.cetys.software_quality_lab.petstore;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    boolean existsByPetIdAndStatus(Long petId, AdoptionStatus status);
    long countByUserIdAndStatus(Long userId, AdoptionStatus status);
}
