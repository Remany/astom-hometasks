package ru.romanov.tasks.task2.service.impl;

import ru.romanov.tasks.task2.dto.ConsumerDto;
import ru.romanov.tasks.task2.exceptions.ResourceNotFoundException;
import ru.romanov.tasks.task2.mapper.ConsumerMapper;
import ru.romanov.tasks.task2.mapper.ConsumerMapperImpl;
import ru.romanov.tasks.task2.repository.ConsumerRepository;
import ru.romanov.tasks.task2.repository.OrderRepository;
import ru.romanov.tasks.task2.repository.impl.ConsumerRepositoryImpl;
import ru.romanov.tasks.task2.repository.impl.OrderRepositoryImpl;
import ru.romanov.tasks.task2.service.ConsumerService;

import java.util.Optional;
import java.util.UUID;

public class ConsumerServiceImpl implements ConsumerService {
    private final ConsumerRepository consumerRepository;
    private final OrderRepository orderRepository;
    private final ConsumerMapper consumerMapper;

    public ConsumerServiceImpl() {
        this.consumerRepository = new ConsumerRepositoryImpl();
        this.orderRepository = new OrderRepositoryImpl();
        this.consumerMapper = new ConsumerMapperImpl();
    }

    public ConsumerServiceImpl(ConsumerRepository consumerRepository, OrderRepository orderRepository, ConsumerMapper consumerMapper) {
        this.consumerRepository = consumerRepository;
        this.orderRepository = orderRepository;
        this.consumerMapper = consumerMapper;
    }

    @Override
    public void createConsumer(ConsumerDto consumerDto) {
        consumerRepository.createConsumer(consumerMapper.toConsumer(consumerDto));
    }

    @Override
    public ConsumerDto getConsumer(UUID id) {
        return consumerRepository.getConsumerById(id)
                .map(consumerMapper::toConsumerDto)
                .flatMap(consumerDto -> {
                    consumerDto.setOrders(orderRepository.getOrdersOfOwner(id));
                    return Optional.of(consumerDto);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found"));
    }

    @Override
    public void buyProduct(UUID consumerId, UUID productId) {
        consumerRepository.buyProduct(consumerId, productId);
    }
}
