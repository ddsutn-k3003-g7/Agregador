package ar.edu.utn.dds.k3003.app.strategies;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.utn.dds.k3003.model.ConsensosEnum;
import ar.edu.utn.dds.k3003.app.strategies.ConsensoTodosStrategy;
import ar.edu.utn.dds.k3003.app.strategies.ConsensoAlMenosDosStrategy;
import ar.edu.utn.dds.k3003.app.strategies.ConsensoEstrictoStrategy;


@Component
public class ConsensoStrategyFactory {

    private final Map<ConsensosEnum, ConsensoStrategy> strategies;
        private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;
    @Autowired
    public ConsensoStrategyFactory(List<ConsensoStrategy> strategyList, ObjectMapper objectMapper, ApplicationContext applicationContext) {
        strategies = new EnumMap<>(ConsensosEnum.class);
        this.applicationContext = applicationContext;
        this.objectMapper = objectMapper;
        
        // Mapear cada estrategia a su enum correspondiente
        strategyList.forEach(strategy -> {
            if (strategy instanceof ConsensoTodosStrategy) {
                strategies.put(ConsensosEnum.TODOS, strategy);
            } else if (strategy instanceof ConsensoAlMenosDosStrategy) {
                strategies.put(ConsensosEnum.AL_MENOS_2, strategy);
            } else if (strategy instanceof ConsensoEstrictoStrategy) {
                strategies.put(ConsensosEnum.ESTRICTO, strategy);
            }
        });
    }
    
    public ConsensoStrategy getStrategy(ConsensosEnum tipoConsenso) {
        ConsensoStrategy strategy = strategies.get(tipoConsenso);
        if (strategy == null) {
        throw new IllegalArgumentException("Tipo de consenso no soportado: " + tipoConsenso);
        }
        return strategy;
    }
}
