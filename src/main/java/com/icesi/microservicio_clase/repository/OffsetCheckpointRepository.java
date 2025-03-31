package com.icesi.microservicio_clase.repository;

import com.icesi.microservicio_clase.model.OffsetCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffsetCheckpointRepository extends JpaRepository<OffsetCheckpoint, OffsetCheckpoint.OffsetCheckpointId> {
}
