package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Fuente;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;

@Repository
@Profile("test")
public class InMemoryFuenteRepo implements FuenteRepository {

    private List<Fuente> fuentes;
    private static int contador = 0;

    public InMemoryFuenteRepo() {
        this.fuentes = new ArrayList<>();
    }

    @Override
    public Optional<Fuente> findById(String id) {
        return fuentes.stream()
                .filter(fuente -> fuente.getId().equals(id))
                .findFirst();
    }

    @Override
    public Fuente save(Fuente fuente) {
        fuente.setId(String.valueOf(++contador)); // Asigna un ID único
        this.fuentes.add(fuente);
        return fuente;
    }

    @Override
    public List<Fuente> findAll() {
        return new ArrayList<>(fuentes);
    }

    @Override
    public int countBy() {
        return fuentes.size();
    }
    
}
