package ar.edi.itn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.Application;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FuenteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void listarFuentes_DeberiaRetornarListaVacia() throws Exception {
        mockMvc.perform(get("/api/fuentes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    @Test
    public void listarFuentes_DeberiaRetornarUnaLista() throws Exception {

        mockMvc.perform(post("/api/fuentes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new FuenteDTO("", "Fuente1", "123"))));

        mockMvc.perform(post("/api/fuentes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new FuenteDTO("", "Fuente2", "456"))));

        mockMvc.perform(get("/api/fuentes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
} 