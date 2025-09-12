package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.config.MetricsService;
import ar.edu.utn.dds.k3003.facades.FachadaAgregador;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import io.micrometer.core.instrument.Timer;

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
    private final MetricsService metricsService;

    @Autowired
    public FuenteController(FachadaAgregador fachadaAgregador, MetricsService metricsService) {
        this.fachadaAgregador = fachadaAgregador;
        this.metricsService = metricsService;
    }

    @GetMapping
    public ResponseEntity<List<FuenteDTO>> listarFuentes() {
        Timer.Sample timer = metricsService.startTimer();

        try {
            return ResponseEntity.ok(fachadaAgregador.fuentes());
        } finally {
            metricsService.stopTimer(timer, "get_fuentes.tiempo_consulta",
                    "service", "agregador_api");
        }

    }
    
    @PostMapping
    public ResponseEntity<FuenteDTO> agregarFuente(@RequestBody FuenteDTO fuente) {

        Timer.Sample timer = metricsService.startTimer();

        try {
            return ResponseEntity.ok(fachadaAgregador.agregar(fuente));
        } finally {
            metricsService.stopTimer(timer, "post_fuentes.tiempo_consulta",
                    "service", "agregador_api");
        }

    }
    
}
