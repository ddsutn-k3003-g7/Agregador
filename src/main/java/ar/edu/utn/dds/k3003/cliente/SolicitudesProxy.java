package ar.edu.utn.dds.k3003.cliente;

import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;

import java.lang.reflect.Method;
import java.util.*;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import okhttp3.OkHttpClient;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class SolicitudesProxy implements FachadaSolicitudes{
    
    private final String endpoint;
    private final SolicitudesRetrofitClient service;

    public SolicitudesProxy(ObjectMapper objectMapper) {

    var env = System.getenv();
    this.endpoint = env.getOrDefault("URL_SOLICITUDES", "https://two025-tp-entrega-3-solicitudes.onrender.com/");
 
     // Configurar OkHttpClient con logging y timeouts
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
          
            if (objectMapper != null) {
              //throw new IllegalStateException("Service no inicializado");
            }

    var retrofit =
        new Retrofit.Builder()
            .baseUrl(this.endpoint)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build();

    this.service = retrofit.create(SolicitudesRetrofitClient.class);
  }

  @Override
 public SolicitudDTO agregar(SolicitudDTO solicitudDTO){
    return null;
 }

  @Override
  public SolicitudDTO modificar(String solicitudId, EstadoSolicitudBorradoEnum esta,
      String descripcion) throws NoSuchElementException{
        return null;
      }

  @SneakyThrows
  @Override
  public List<SolicitudDTO> buscarSolicitudXHecho(String hechoId) throws NoSuchElementException{
    log.info("Proxy: buscando solicitudes por hecho: {}", hechoId);
    
        log.info("Creando llamada al servicio Solicitudes...");

        log.info("2. Service methods available:");
        Method[] methods = this.service.getClass().getDeclaredMethods();
        for (Method method : methods) {
            log.info("   - {}", method.getName());
        }

        if (this.service == null) {
          log.error("❌ SERVICE ES NULL! No se inicializó correctamente");
          throw new IllegalStateException("Service no inicializado");
        }
        log.info("Endpoint base: {}", this.endpoint);
        //Response<List<SolicitudDTO>> response = this.service.get(hechoId).execute();
        log.info("ANTES de execute() - hechoId: {}", hechoId);
        Call<List<SolicitudDTO>> call = this.service.get(hechoId);

        log.info("🚀 EJECUTANDO call.execute()...");
        Response<List<SolicitudDTO>> response = call.execute();

        log.info("Respuesta recibida, código: {}", response.code());
        
        if (!response.isSuccessful()) {
            log.error("Respuesta no exitosa. Código: {}, Mensaje: {}", response.code(), response.message());
            throw new NoSuchElementException("Colección no encontrada. Código: " + response.code());
        }
        
        log.info("Respuesta exitosa. Procesando body...");
        List<SolicitudDTO> body = response.body();
        log.info("Body procesado. Hechos encontrados: {}", body != null ? body.size() : 0);
        
        return body;
  }

  @Override
  public SolicitudDTO buscarSolicitudXId(String solicitudId){
        return null;
      }

  @Override
  public boolean estaActivo(String unHechoId){
        return false;
      }

  @Override
  public void setFachadaFuente(FachadaFuente fuente){
      }

}
