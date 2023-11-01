package com.example.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
@NoArgsConstructor
public class Message implements Comparable<Message> {

    @PrimaryKeyColumn(name = "message_id", type = PrimaryKeyType.CLUSTERED, ordinal = 2, ordering = Ordering.DESCENDING)
    private String messageId;
    @PrimaryKeyColumn(name = "sender_id", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private Long senderId;
    @PrimaryKeyColumn(name = "receiver_id", type = PrimaryKeyType.PARTITIONED, ordinal = 1)
    private Long receiverId;
    @Column("text")
    private String text;
    @Column("send_time")
    private LocalDateTime sendTime;

    public Message(String messageId, Long senderId, Long receiverId, String text, LocalDateTime sendTime) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.sendTime = sendTime.truncatedTo(MINUTES);
    }

    public LocalDateTime getSendTime() {
        return sendTime.truncatedTo(MINUTES);
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime.truncatedTo(MINUTES);
    }

    @Override
    public int compareTo(@NotNull Message o) {
        return o.sendTime.compareTo(this.sendTime);
    }
}
