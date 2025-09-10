package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/coleccion")
public class ColeccionController {

    private final FachadaAgregador fachadaAgregador;

    @Autowired
    public ColeccionController(FachadaAgregador fachadaAgregador) {
        this.fachadaAgregador = fachadaAgregador;
    }

    @GetMapping("/{nombre}/hechos")
    public ResponseEntity<List<HechoDTO>> hechosDeColeccion(
            @PathVariable String nombre) {

        List<HechoDTO> hechos = fachadaAgregador.hechos(nombre);
        log.info("Hechos encontrados: {}", hechos.size());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(hechos);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.ok(hechos);
    }
}
