package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Consenso;
//import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.model.ConsensosEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;

@Repository
@Profile("test")
public class InMemoryConsensoRepo implements ConsensoRepository {

    private List<Consenso> consensos;
    public InMemoryConsensoRepo() {
        this.consensos = new ArrayList<>();
    }

    @Override
    public Optional<Consenso> findById(String coleccionId) {
        return this.consensos.stream()
                .filter(consenso -> consenso.getColeccionId().equals(coleccionId))
                .findFirst();
    }

    @Override
    public Consenso saveConsenso(Consenso consenso, String coleccionId) {
        consenso.setColeccionId(coleccionId);
        this.consensos.add(consenso);
        return consenso;
    }

    @Override
    public ConsensosEnum findConsensoByColeccionId(String coleccionId) {
        return this.findById(coleccionId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el consenso para la colección: " + coleccionId))
                .getConsenso();

    }

}
