package ar.edu.utn.dds.k3003.cliente;

import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;

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
public class FuentesProxy implements FachadaFuente{
    
    private final String endpoint;
    private final FuentesRetrofitClient service;

    public FuentesProxy(ObjectMapper objectMapper, String endPoint) {

    var env = System.getenv();
    //this.endpoint = env.getOrDefault("URL_FUENTES", "https://two025-tp-entrega-2-larasnr.onrender.com");
    this.endpoint = endPoint;

     // Configurar OkHttpClient con logging y timeouts
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    var retrofit =
        new Retrofit.Builder()
            .baseUrl(this.endpoint)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build();

    this.service = retrofit.create(FuentesRetrofitClient.class);
  }

  @Override
  public List<ColeccionDTO> colecciones() {

    return Collections.emptyList();
  }

    @Override
  public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
   
    return null;
  }

  @Override
  public ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException {
    return null;
  }
  
  @Override
  public HechoDTO agregar(HechoDTO hechoDTO) {
    return null;
  }
  
  @Override
  public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
    return null;
  }
  
  @SneakyThrows
  @Override
  public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException {
    /* 
    log.info("Proxy: buscando hechos por coleccion");
    Response<List<HechoDTO>> response = service.get(coleccionId).execute();
    if (!response.isSuccessful()) {
      log.error("la respuesta no fue exitosa");
      throw new NoSuchElementException("Colección no encontrada");
    }
    log.info("la respuesta fue exitosa");
    return response.body();
    */
    log.info("Proxy: buscando hechos por coleccion: {}", coleccionId);
    
        log.info("Creando llamada al servicio...");

        Response<List<HechoDTO>> response = this.service.get(coleccionId).execute();

        log.info("Respuesta recibida, código: {}", response.code());
        
        if (!response.isSuccessful()) {
            log.error("Respuesta no exitosa. Código: {}, Mensaje: {}", response.code(), response.message());
            throw new NoSuchElementException("Colección no encontrada. Código: " + response.code());
        }
        
        log.info("Respuesta exitosa. Procesando body...");
        List<HechoDTO> body = response.body();
        log.info("Body procesado. Hechos encontrados: {}", body != null ? body.size() : 0);
        
        return body;
  }

  @Override
  public void setProcesadorPdI(FachadaProcesadorPdI procesador) {

  }

  @Override
  public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
    return null;
  }

}
