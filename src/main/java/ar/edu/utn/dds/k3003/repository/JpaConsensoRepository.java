package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Consenso;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import java.util.Optional;

@Repository
@Profile("!test")
public interface JpaConsensoRepository extends JpaRepository<Consenso, String>, ConsensoRepository {
    @Override
    Optional<Consenso> findById(String coleccionId);
    // Método para actualizar o crear
    default Consenso saveConsenso(Consenso consenso, String coleccionId) {
        consenso.setColeccionId(coleccionId);
        return save(consenso); // Usa el save automático de JpaRepository
    }

    // ✅ Método que retorna la entidad completa (esto funciona)
    Optional<Consenso> findByColeccionId(String coleccionId);
    
    // ❌ ELIMINA ESTE MÉTODO - causa problemas con Optional<Enum>
    // @Query("SELECT c.tipoConsenso FROM Consenso c WHERE c.coleccionId = :coleccionId")
    // Optional<ConsensosEnum> findTipoConsensoByColeccionId(@Param("coleccionId") String coleccionId);
    
    // ✅ Implementación default para la interfaz ConsensoRepository
    @Override
    default ConsensosEnum findConsensoByColeccionId(String coleccionId) {
        Optional<Consenso> consensoOpt = findByColeccionId(coleccionId);
        return consensoOpt.map(Consenso::getConsenso).orElse(null);
    }
    
}