package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.model.ConsensosEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Data
@Entity
@NoArgsConstructor // Lombok
public class Consenso {

    public Consenso(ConsensosEnum consenso, String coleccionId) {
        this.consenso = consenso;
        this.coleccionId = coleccionId;
    }

    @Enumerated(EnumType.STRING)  // Para guardar el enum como texto en la BD
    private ConsensosEnum consenso;
    @Id
    private String coleccionId;

}
