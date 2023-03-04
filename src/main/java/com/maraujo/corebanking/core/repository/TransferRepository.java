package com.maraujo.corebanking.core.repository;

import com.maraujo.corebanking.core.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<TransferEntity, UUID> {
}
