package com.emmanueljeanpierreauguste.riubackend.infrastructure.config;

import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.dto.SearchEventDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConfigTest {

    @Test
    @DisplayName("Should create producer factory, consumer factory, kafka template and listener factory")
    void shouldCreateAllBeans() throws Exception {
        KafkaConfig config = new KafkaConfig();

        Field field = KafkaConfig.class.getDeclaredField("bootstrapServers");
        field.setAccessible(true);
        field.set(config, "localhost:9092");

        ProducerFactory<String, SearchEventDto> producerFactory = config.producerFactory();
        KafkaTemplate<String, SearchEventDto> kafkaTemplate = config.kafkaTemplate();
        ConsumerFactory<String, SearchEventDto> consumerFactory = config.consumerFactory();
        var listenerFactory = config.kafkaListenerContainerFactory();

        assertAll(
                () -> assertNotNull(producerFactory),
                () -> assertNotNull(kafkaTemplate),
                () -> assertNotNull(consumerFactory),
                () -> assertNotNull(listenerFactory)
        );
    }
}

