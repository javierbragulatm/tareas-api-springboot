
package com.javi.tareasAPI.messaging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.javi.tareasAPI.event.TareaCreadaEvent;

@Component
@ConditionalOnProperty(
        name = "app.kafka.enabled",
        havingValue = "false"
)
public class NoOpTareaEventPublisher implements TareaEventPublisher {

    @Override
    public void publicarTareaCreada(TareaCreadaEvent evento) {
        // No se publican eventos cuando Kafka está desactivado.
    }
}
