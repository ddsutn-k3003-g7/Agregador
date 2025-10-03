package ar.edu.utn.dds.k3003.app.strategies;

import java.util.List;
import org.springframework.stereotype.Component;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;


@Component
public class ConsensoTodosStrategy implements ConsensoStrategy{
   @Override
   public List<HechoDTO> aplicarConsenso(List<HechoDTO> hechos){
    return hechos;
   }
}
