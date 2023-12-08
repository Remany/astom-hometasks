package ru.romanov.tasks.task2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    private UUID id;
    private UUID productId;
    private BigDecimal value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
