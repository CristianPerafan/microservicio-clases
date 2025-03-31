package com.icesi.microservicio_clase.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@IdClass(OffsetCheckpoint.OffsetCheckpointId.class)
public class OffsetCheckpoint {

    @Id
    private String topic;
    @Id
    private int partition;
    private long offsetValue;

    public OffsetCheckpoint() {}

    public OffsetCheckpoint(String topic, int partition, long offsetValue) {
        this.topic = topic;
        this.partition = partition;
        this.offsetValue = offsetValue;
    }


    @Setter
    @Getter
    public static class OffsetCheckpointId implements Serializable {
        private String topic;
        private int partition;

        public OffsetCheckpointId() {}

        public OffsetCheckpointId(String topic, int partition) {
            this.topic = topic;
            this.partition = partition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OffsetCheckpointId that = (OffsetCheckpointId) o;
            return partition == that.partition && topic.equals(that.topic);
        }

        @Override
        public int hashCode() {
            return 31 * topic.hashCode() + partition;
        }
    }
}