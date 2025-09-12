package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.config.MetricsService;
import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

@Slf4j
@RestController
@RequestMapping("/api/consenso")
public class ConsensoController {

    private final FachadaAgregador fachadaAgregador;
    private final MetricsService metricsService;

    @Autowired
    public ConsensoController(FachadaAgregador fachadaAgregador, MetricsService metricsService) {
        this.fachadaAgregador = fachadaAgregador;
        this.metricsService = metricsService;
    }

    @PatchMapping
    public ResponseEntity<Void> actualizarEstrategiaConsenso(
            @RequestBody ConsensoRequest request) {

        log.info("Request recibido: tipoConsenso={}, coleccionId={}",
                request.getTipoConsenso(), request.getColeccionId());

        Timer.Sample timer = metricsService.startTimer();
        try {
            fachadaAgregador.setConsensoStrategy(
                    request.getTipoConsenso(),
                    request.getColeccionId()

            );
            return ResponseEntity.noContent().build();
        } finally {
            metricsService.stopTimer(timer, "agregador.patch_consenso.tiempo_consulta",
                    "service", "agregador_api");
            metricsService.incrementCounter("agregador.patch_consenso.contador",
                    "service", "agregador_api");
        }

    }

    // Clase interna para el request body
    public static class ConsensoRequest {

        @JsonProperty("tipoConsenso")
        private ConsensosEnum tipoConsenso;

        @JsonProperty("coleccionId")
        private String coleccionId;

        public ConsensoRequest() {}

        // Getters y setters
        public ConsensosEnum getTipoConsenso() {
            return tipoConsenso;
        }

        public void setTipoConsenso(ConsensosEnum tipoConsenso) {
            this.tipoConsenso = tipoConsenso;
        }

        public String getColeccionId() {
            return coleccionId;
        }

        public void setColeccionId(String coleccionId) {
            this.coleccionId = coleccionId;
        }
    }
}