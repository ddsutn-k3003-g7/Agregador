package ar.edu.utn.dds.k3003.app.strategies;

import java.util.List;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;

public interface ConsensoStrategy {
    List<HechoDTO> aplicarConsenso(List<HechoDTO> hechos);
}
