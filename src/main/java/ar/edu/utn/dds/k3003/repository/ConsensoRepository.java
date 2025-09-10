package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Consenso;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsensoRepository {
    Optional<Consenso> findById(String coleccionId);
    Consenso saveConsenso(Consenso consenso, String coleccionId);
    ConsensosEnum findConsensoByColeccionId(String coleccionId);

}
