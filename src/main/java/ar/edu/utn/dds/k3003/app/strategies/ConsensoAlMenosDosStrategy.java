package ar.edu.utn.dds.k3003.app.strategies;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;


@Component
public class ConsensoAlMenosDosStrategy implements ConsensoStrategy{
   @Override
   public List<HechoDTO> aplicarConsenso(List<HechoDTO> hechos){
      // Paso 1: Contar ocurrencias de cada título
            Map<String, Long> conteoTitulos = hechos.stream()
                    .filter(hecho -> hecho.titulo() != null && !hecho.titulo().isEmpty())
                    .collect(Collectors.groupingBy(
                            HechoDTO::titulo,
                            Collectors.counting()));


            // Paso 2: Filtrar para mantener solo los que tienen conteo > 1 (duplicados)
            List<HechoDTO> hechosDuplicados = hechos.stream()
                    .filter(hecho -> hecho.titulo() != null &&
                            !hecho.titulo().isEmpty() &&
                            conteoTitulos.getOrDefault(hecho.titulo(), 0L) > 1)
                    .collect(Collectors.toList());

            hechos = hechosDuplicados;
    return hechos;
   }
}
