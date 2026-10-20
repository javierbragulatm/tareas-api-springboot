
package com.javi.tareasAPI.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;

@Configuration
@ConditionalOnProperty(
        name = "app.kafka.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class KafkaTopicConfig {

    @Bean
    public NewTopic tareasCreadasTopic(
            @Value("${app.kafka.topic.tareas-creadas}") String topic) {

        return TopicBuilder.name(topic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
