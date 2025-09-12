package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.config.MetricsService;
import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import io.micrometer.core.instrument.Timer;
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
    private final MetricsService metricsService;

    @Autowired
    public ColeccionController(FachadaAgregador fachadaAgregador, MetricsService metricsService) {
        this.fachadaAgregador = fachadaAgregador;
        this.metricsService = metricsService;
    }

    @GetMapping("/{nombre}/hechos")
    public ResponseEntity<List<HechoDTO>> hechosDeColeccion(
            @PathVariable String nombre) {

        Timer.Sample timer = metricsService.startTimer();

        try{
        List<HechoDTO> hechos = fachadaAgregador.hechos(nombre);
        log.info("Hechos encontrados: {}", hechos.size());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(hechos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(hechos);
        }finally{
            //metricsService.stopTimer(timer, nombre, null);
            metricsService.stopTimer(timer, "hechos.tiempo_consulta", 
                "service", "agregador_api");
        }


    }
}
