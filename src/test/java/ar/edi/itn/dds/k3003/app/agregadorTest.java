package ar.edi.itn.dds.k3003.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.service.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.model.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import java.util.List;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class agregadorTest {

  private static final String NOMBRE_FUENTE = "nombre1";

  FachadaAgregador instancia;
  @Mock
  FachadaFuente fuente1;
  @Mock
  FachadaFuente fuente2;


  @SneakyThrows
  @BeforeEach
  void setUp() {
    instancia = new Fachada();

  }

  @Test
  @DisplayName("agregar()")
  void testAgregarFuente() {
    val fuenteDTO = instancia.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    assertNotNull(
        fuenteDTO.id(),
        "#agregar debe retornar un FuenteDTO con un id inicializado.");

    val fuente1 = instancia.buscarFuenteXId(fuenteDTO.id());
    assertEquals(
        NOMBRE_FUENTE,
        fuente1.nombre(),
        "No se esta recuperando el nombre de la fuente correctamente.");
  }


  @Test
  @DisplayName("fuentes()")
  void testFuentes() {

    assertEquals(
        0,
        instancia.fuentes().size(),
        "Fuentes no debe tener elementos al inicio.");

    instancia.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    instancia.agregar(new FuenteDTO("", "nombre2", "123"));

    assertEquals(
        2,
        instancia.fuentes().size(),
        "No se esta recuperando el nombre de la fuente correctamente.");
  }

  @Test
  @DisplayName("buscarFuenteXId()")
  void testBuscarFuente() {
      val fuenteDTO = instancia.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
      
      val fuente1 = instancia.buscarFuenteXId(fuenteDTO.id());
      assertEquals(
          NOMBRE_FUENTE,
          fuente1.nombre(),
          "No se esta recuperando el nombre de la fuente correctamente.");

      val fuenteDTO2 = instancia.agregar(new FuenteDTO("", "nombre2", "123"));
        val fuente2 = instancia.buscarFuenteXId(fuenteDTO2.id());
        assertEquals(
            "nombre2",
            fuente2.nombre(),
            "No se esta recuperando el nombre de la segunda fuente correctamente.");

  }

  @Test
  @DisplayName("Busqueda de hechos con consenso 'TODO'")
  void testBuscarHechosConsensoTodo() {

    initializeFuentes(ConsensosEnum.TODOS);

    val titulos = instancia.hechos("1").stream().map(HechoDTO::titulo).toList();

    assertEquals(4,
            titulos.size(),
            "El agregador con consenso TODO, esta retornando titulos que no deberia.");
    assertTrue(
        titulos.containsAll(List.of("a", "b", "c", "d")),
        "El agregador no retorna todos los hechos que deberia para el consenso TODO.");

  }

  @Test
  @DisplayName("Busqueda de hechos con consenso 'AL_MENOS_2'")
  void testBuscarHechosConsensoAlMenos2() {

    initializeFuentes(ConsensosEnum.AL_MENOS_2);

    val titulos = instancia.hechos("1").stream().map(HechoDTO::titulo).toList();

    assertEquals(2,
            titulos.size(),
            "El agregador con concenso menos 2, esta retornando titulos que no deberia.");
    assertTrue(
        titulos.containsAll(List.of("a", "b")),
        "El agregador no retorna todos los hechos que deberia para el consenso al menos 2.");

  }

  private void initializeFuentes(ConsensosEnum consenso) {
    val fuenteDTO1 = instancia.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    instancia.addFachadaFuentes(fuenteDTO1.id(), fuente1);
    val fuenteDTO2 = instancia.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    instancia.addFachadaFuentes(fuenteDTO2.id(), fuente2);
    instancia.setConsensoStrategy(consenso, "1");
    when(fuente1.buscarHechosXColeccion("1")).thenReturn(List.of(
        new HechoDTO("1", "1","a"), new HechoDTO("2", "1","b"), new HechoDTO("3", "1","c")
    ));
    when(fuente2.buscarHechosXColeccion("1")).thenReturn(List.of(
        new HechoDTO("4", "1","a"), new HechoDTO("5", "1","b" ), new HechoDTO("6", "1","d")
    ));
  }



}