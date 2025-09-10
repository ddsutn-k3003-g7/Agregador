package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/fuentes")
public class FuenteController {

    private final FachadaAgregador fachadaAgregador;

    @Autowired
    public FuenteController(FachadaAgregador fachadaAgregador) {
        this.fachadaAgregador = fachadaAgregador;
    }

    @GetMapping
    public ResponseEntity<List<FuenteDTO>> listarFuentes() {
        return ResponseEntity.ok(fachadaAgregador.fuentes());
    }
    
    @PostMapping
    public ResponseEntity<FuenteDTO> agregarFuente(@RequestBody FuenteDTO fuente) {
        return ResponseEntity.ok(fachadaAgregador.agregar(fuente));
    }
    
}
