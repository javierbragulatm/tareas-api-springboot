
package com.javi.tareasAPI.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.javi.tareasAPI.event.TareaCreadaEvent;

@Component
@ConditionalOnProperty(
        name = "app.kafka.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class TareaCreadaConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(TareaCreadaConsumer.class);

    @KafkaListener(topics = "${app.kafka.topic.tareas-creadas}")
    public void consumir(TareaCreadaEvent evento) {

        log.info(
                "Evento procesado: tarea id={}, titulo='{}', usuario={}, completada={}",
                evento.id(),
                evento.titulo(),
                evento.username(),
                evento.completada()
        );
    }
}
