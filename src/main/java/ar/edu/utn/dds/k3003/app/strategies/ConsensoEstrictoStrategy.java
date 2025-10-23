package ar.edu.utn.dds.k3003.app.strategies;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.utn.dds.k3003.model.HechoDTO;
import lombok.extern.slf4j.Slf4j;
import ar.edu.utn.dds.k3003.cliente.SolicitudesProxy;

@Slf4j
@Component
public class ConsensoEstrictoStrategy implements ConsensoStrategy{
    private final SolicitudesProxy solicitudesProxy;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public ConsensoEstrictoStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        // Crear el proxy inyectando ObjectMapper
       this.solicitudesProxy = new SolicitudesProxy(objectMapper);
    }
   @Override
   public List<HechoDTO> aplicarConsenso(List<HechoDTO> hechos){
        log.info("llamando a solitudes por los hechos");
      hechos = hechos.stream()
               .filter(hecho -> {
                return estaActivo(hecho);
               })
               .collect(Collectors.toList());;
    return hechos;
   }
   private boolean estaActivo(HechoDTO hecho) {
    return solicitudesProxy.buscarSolicitudXHecho(hecho.id()).isEmpty();
   }
}
