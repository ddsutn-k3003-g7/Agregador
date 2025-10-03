package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.app.strategies.ConsensoStrategy;
import ar.edu.utn.dds.k3003.app.strategies.ConsensoStrategyFactory;
import ar.edu.utn.dds.k3003.cliente.FuentesProxy;
import ar.edu.utn.dds.k3003.config.GlobalExceptionHandler;
import ar.edu.utn.dds.k3003.service.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.model.Consenso;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import ar.edu.utn.dds.k3003.repository.ConsensoRepository;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;
import ar.edu.utn.dds.k3003.repository.InMemoryConsensoRepo;
import ar.edu.utn.dds.k3003.model.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import java.security.InvalidParameterException;
import java.util.Optional;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Fachada implements FachadaAgregador {


    private ObjectMapper objectMapper;
    private final FuenteRepository fuenteRepository;
    private final ConsensoRepository consensoRepository; // Inicializa el repositorio de consensos en memoria
    private ConsensoStrategyFactory strategyFactory;
    private Map<String, FachadaFuente> fachadas = new HashMap<>();
    public Fachada() {
        this.fuenteRepository = new InMemoryFuenteRepo(); // Inicializa el repositorio de fuentes en memoria
        this.consensoRepository = new InMemoryConsensoRepo(); // Inicializa el repositorio de consensos en memoria
    }

    @Autowired
    public Fachada(FuenteRepository fuenteRepository, ConsensoRepository consensoRepository, ConsensoStrategyFactory strategyFactory) {
        this.fuenteRepository = fuenteRepository; // Inyecta el repositorio de fuentes
        this.consensoRepository = consensoRepository; // Inyecta el repositorio de consensos
        
        //setear objectMapper
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);

        this.strategyFactory = strategyFactory;
        
        //cargar fuentes desde postgres
        List<Fuente> fuentes = fuenteRepository.findAll();
        fachadas.clear(); // Limpiar mapa existente
        
        fuentes.forEach(fuente -> {
            FachadaFuente fachada = new FuentesProxy(
            objectMapper,
            fuente.getEndpoint()
        );
            fachadas.put(fuente.getId(), fachada);
        });
        
        log.info("Cargadas {} fachadas desde la base de datos", fachadas.size());
    }

    @Override
    public FuenteDTO agregar(FuenteDTO fuente) {
        if (this.fuenteRepository.findById(fuente.id()).isPresent()) {
            log.error("ya existe una fuente con ese id");
            throw new IllegalArgumentException("Ya existe una fuente con el ID: " + fuente.id());
        }
        var nuevaFuente = this.fuenteRepository.save(new Fuente(fuente.id(), fuente.nombre(), fuente.endpoint())); // Guarda la nueva fuente en el repositorio
        FuentesProxy nuevoProxy = new FuentesProxy(objectMapper, fuente.endpoint());
        this.addFachadaFuentes(nuevaFuente.getId(), nuevoProxy);
        return new FuenteDTO(nuevaFuente.getId(), nuevaFuente.getNombre(), nuevaFuente.getEndpoint());
    }

     public List<FuenteDTO> fuentes(){
        List<Fuente> fuentes = fuenteRepository.findAll();
        // Convierte la lista de fuentes a DTOs
        return fuentes.stream()
                .map(fuente -> new FuenteDTO(fuente.getId(), fuente.getNombre(), fuente.getEndpoint()))
                .toList();
     }

    @Override
    public FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException{
        List<FuenteDTO> fuentes = this.fuentes();
        return fuentes.stream()
                .filter(fuente -> fuente.id().equals(fuenteId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No se encontró la fuente con ID: " + fuenteId));
    };
    
    @Override
    public List<HechoDTO> hechos(String coleccionId) throws NoSuchElementException{
        log.info("buscando hechos...");
       
        ConsensosEnum tipoConsenso = null;
        try {
            tipoConsenso = this.consensoRepository.findConsensoByColeccionId(coleccionId);

            log.info("Consenso encontrado: {}", tipoConsenso);
        } catch (Exception e) {
            log.error("ERROR en findConsensoByColeccionId: {}", e.getMessage(), e);
            tipoConsenso = ConsensosEnum.TODOS;
        }

        if (tipoConsenso == null) {
            log.info("No se encontro consenso, default -> TODOS");
            tipoConsenso = ConsensosEnum.TODOS;
        }

        //obtener las fachadas de fuentes
        log.info("obteniendo fachadas");
        if (fachadas.isEmpty()) {
            
        }
        List<FachadaFuente> listaFachadas = new ArrayList<>(fachadas.values());
        if (listaFachadas.size() == 0) {
            log.info("no hay fachadas en la lista");
        }
        log.info("fachadas encontradas: {}", fachadas.size());
        List<HechoDTO> hechos = listaFachadas.stream().map(fachada -> fachada.buscarHechosXColeccion(coleccionId))
                .flatMap(List::stream)
                .toList();

        log.info("hechos encontrados: {}", hechos.size());

        ConsensoStrategy strategy = strategyFactory.getStrategy(tipoConsenso);

        // aplicar el consenso
         if (tipoConsenso == ConsensosEnum.AL_MENOS_2 && fachadas.size() < 2) {
            log.warn("Consenso AL_MENOS_2 requerido pero solo hay {} fuentes", fachadas.size());
            strategy = strategyFactory.getStrategy(ConsensosEnum.TODOS);
        } 
        
        hechos = strategy.aplicarConsenso(hechos);

        // Eliminar duplicados 
        //if (tipoConsenso == ConsensosEnum.AL_MENOS_2 || tipoConsenso == ConsensosEnum.TODOS) {
            Set<String> titulosVistos = new HashSet<>();
            hechos = hechos.stream()
                    .filter(hecho -> {
                        if (hecho.titulo() == null || hecho.titulo().isEmpty()) {
                            log.warn("Hecho sin título, filtrando: {}", hecho);
                            return false;
                        }
                        boolean esNuevo = titulosVistos.add(hecho.titulo());
                        if (!esNuevo) {
                            log.debug("Filtrando duplicado: {}", hecho.titulo());
                        }
                        return esNuevo;
                    })
                    .collect(Collectors.toList());

            log.info("Hechos finales después de eliminar duplicados: {}", hechos.size());
        //}

        return hechos;
    };

    @Override
    public void addFachadaFuentes(String fuenteId, FachadaFuente fuente){
        if (fachadas.containsKey(fuenteId)) {
            //log.error("ya hay una fachada con ese id");
            //throw new IllegalArgumentException("Ya existe una fachada para la fuente con ID: " + fuenteId);
        }
        fachadas.put(fuenteId, fuente);
    };

    @Override
    public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId) throws InvalidParameterException{
        log.info("seteando el consenso");
        // 1. Validación de parámetros
    if (tipoConsenso == null) {
        log.info("el tipo de consenso es nulo");
        throw new InvalidParameterException("El tipo de consenso no puede ser nulo");
    }
    
    if (coleccionId == null || coleccionId.trim().isEmpty()) {
        log.info("la coleccion no existe");
        throw new InvalidParameterException("El ID de colección no puede ser nulo o vacío");
    }
        // 2. Verificar si ya existe un consenso para la colección
    val existingConsenso = this.consensoRepository.findById(coleccionId);
    if (existingConsenso.isPresent()) {
        log.info("ya existe el consenso para la coleccion");
    }
    
        // 3. Crear y guardar el nuevo consenso 
    log.info("creando el consenso");
    Consenso nuevoConsenso = new Consenso(tipoConsenso, coleccionId);
    log.info("guardando el nuevo consenso");
    this.consensoRepository.saveConsenso(nuevoConsenso, coleccionId);

    val consenso = this.consensoRepository.findById(coleccionId);
    if (consenso.isEmpty()) {
        throw new NoSuchElementException("No se encontró el consenso para la colección: " + coleccionId);
    };
    }
    
}
