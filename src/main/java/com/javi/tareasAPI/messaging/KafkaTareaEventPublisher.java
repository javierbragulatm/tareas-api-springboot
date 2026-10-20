
package com.javi.tareasAPI.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.javi.tareasAPI.event.TareaCreadaEvent;

@Component
@ConditionalOnProperty(
        name = "app.kafka.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class KafkaTareaEventPublisher implements TareaEventPublisher {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaTareaEventPublisher.class);

    private final KafkaTemplate<String, TareaCreadaEvent> kafkaTemplate;
    private final String topic;

    public KafkaTareaEventPublisher(
            KafkaTemplate<String, TareaCreadaEvent> kafkaTemplate,
            @Value("${app.kafka.topic.tareas-creadas}") String topic) {

        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publicarTareaCreada(TareaCreadaEvent evento) {

        kafkaTemplate.send(topic, evento.id().toString(), evento)
                .whenComplete((resultado, error) -> {
                    if (error != null) {
                        log.error(
                                "No se pudo publicar el evento de la tarea {}",
                                evento.id(),
                                error
                        );
                    } else {
                        log.debug(
                                "Evento de tarea {} publicado en Kafka",
                                evento.id()
                        );
                    }
                });
    }
}
