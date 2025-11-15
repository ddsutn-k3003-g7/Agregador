package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.config.MetricsService;
import ar.edu.utn.dds.k3003.model.HechoDTO;
import ar.edu.utn.dds.k3003.service.FachadaAgregador;

import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
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
            @PathVariable String nombre,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {

        Timer.Sample timer = metricsService.startTimer();

        try{
        List<HechoDTO> hechos = fachadaAgregador.hechos(nombre);
        log.info("Hechos encontrados: {}", hechos.size());
        int fromIndex = 0;
        int toIndex = 0;
        List<HechoDTO> hechosPagina = null;

        // Calcular fromIndex
        fromIndex = page * size;

        // Si fromIndex está fuera de rango, ajustar a la última página disponible
        if (fromIndex >= hechos.size()) {
            // Calcular la última página que tiene datos
            int lastPage = (hechos.size() - 1) / size;
            fromIndex = lastPage * size;
        }

        // Calcular toIndex (nunca debe exceder el tamaño de la lista)
        toIndex = Math.min(fromIndex + size, hechos.size());

        // Asegurarse de que fromIndex no sea mayor que toIndex
        if (fromIndex >= toIndex) {
            hechosPagina = new ArrayList<>();
        } else {
            hechosPagina = hechos.subList(fromIndex, toIndex);
        }
        /*
        if (hechos.size() > (page * size)) {
            fromIndex = Math.min(page * size, hechos.size());
        }else{
            fromIndex = hechos.size() - 1;
        }
        if (hechos.size() > (page * size) + size) {
            toIndex = Math.min((page * size) + size, hechos.size());
        }else if (hechos.size() <= (page * size) + size) {
            toIndex = hechos.size() - 1;
        }
        */

        hechosPagina= hechos.subList(fromIndex, toIndex);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(hechosPagina);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(hechosPagina);
        }finally{
            //metricsService.stopTimer(timer, nombre, null);
            metricsService.stopTimer(timer, "agregador.get_hechos.tiempo_consulta", 
                "service", "agregador_api");
            metricsService.incrementCounter("agregador.get_hechos.contador",
                    "service", "agregador_api");
        }


    }
}