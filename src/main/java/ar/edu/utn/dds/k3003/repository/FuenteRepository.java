package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Fuente;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuenteRepository {
    Optional<Fuente> findById(String id);
    Fuente save(Fuente fuente);
    List<Fuente> findAll();
    int countBy(); // Método para obtener la cantidad de fuentes
}
