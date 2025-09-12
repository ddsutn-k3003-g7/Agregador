package ar.edu.utn.dds.k3003.config;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;

    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    // Contadores
    public void incrementCounter(String metricName, String... tags) {
        Counter counter = Counter.builder(metricName)
                .tags(tags)
                .register(meterRegistry);
        counter.increment();
    }

    // Timers
    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTimer(Timer.Sample sample, String metricName, String... tags) {
        sample.stop(Timer.builder(metricName)
                .tags(tags)
                .register(meterRegistry));
    }

}