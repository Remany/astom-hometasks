package ru.romanov.tasks.task2.repository;

import ru.romanov.tasks.task2.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository {
    List<Order> getOrdersOfOwner(UUID ownerId);
}
