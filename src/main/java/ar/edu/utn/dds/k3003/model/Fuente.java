package ar.edu.utn.dds.k3003.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@NoArgsConstructor // Lombok
public class Fuente {

  public Fuente(String id, String nombre, String endpoint) {
    this.id = id;
    this.nombre = nombre;
    this.endpoint = endpoint;
  }

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  private String id;
  private String nombre;
  private String endpoint;

}