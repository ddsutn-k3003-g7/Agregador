package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Fuente;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@Profile("!test")
public interface JpaFuenteRepository extends JpaRepository<Fuente, String>, FuenteRepository {
    //Optional<Fuente> findByNombre(String nombre);
    //int countBy();
    // Este método cuenta todas las fuentes en la base de datos
}