
package ar.edu.utn.dds.k3003.model;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.utn.dds.k3003.facades.dtos.CategoriaHechoEnum;

public record HechoDTO(String id,String nombreColeccion, String titulo, List<String> etiquetas,
                       CategoriaHechoEnum categoria,
                       String ubicacion, LocalDateTime fecha, String origen, String estado) {

  public HechoDTO(String id,String nombreColeccion, String titulo) {
    this(id, nombreColeccion, titulo, null, null, null, null, null, null);
  }
}
