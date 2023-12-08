package ru.romanov.tasks.task2.service;

import ru.romanov.tasks.task2.dto.ConsumerDto;

import java.util.UUID;

public interface ConsumerService {
    void createConsumer(ConsumerDto consumerDto);

    ConsumerDto getConsumer(UUID id);

    void buyProduct(UUID consumerId, UUID productId);
}
