package ru.romanov.tasks.task2.repository;

import ru.romanov.tasks.task2.entity.Consumer;

import java.util.Optional;
import java.util.UUID;

public interface ConsumerRepository {
    void createConsumer(Consumer consumer);

    Optional<Consumer> getConsumerById(UUID id);

    void buyProduct(UUID consumerId, UUID productId);
}
