package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MINUTES;

@Table("message")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @PrimaryKeyColumn(name = "messageId", type = PrimaryKeyType.CLUSTERED, ordinal = 2, ordering = Ordering.DESCENDING)
    private String messageId;
    @PrimaryKeyColumn(name = "senderId", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private Long senderId;
    @PrimaryKeyColumn(name = "receiverId", type = PrimaryKeyType.PARTITIONED, ordinal = 1)
    private Long receiverId;
    @Column("text")
    private String text;
    @Column("sendTime")
    private LocalDateTime sendTime;

    public LocalDateTime getSendTime() {
        return sendTime.truncatedTo(MINUTES);
    }
}
