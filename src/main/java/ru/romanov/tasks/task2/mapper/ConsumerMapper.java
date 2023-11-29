package ru.romanov.tasks.task2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.romanov.tasks.task2.entity.Consumer;
import ru.romanov.tasks.task2.dto.ConsumerDto;

@Mapper
public interface ConsumerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Consumer toConsumer(ConsumerDto dto);

    @Mapping(target = "orders", ignore = true)
    ConsumerDto toConsumerDto(Consumer consumer);
}
