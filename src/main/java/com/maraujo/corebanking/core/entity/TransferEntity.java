package com.maraujo.corebanking.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "operation_transfer")
public class TransferEntity {

    @Id
    @Column(name = "operation_transfer_id")
    @UuidGenerator
    private UUID operationTransferId;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "account_from")
    private Integer accountFrom;
    @Column(name = "account_to")
    private Integer accountTo;
    @Column(name = "dat_creation")
    private LocalDateTime creationDate;

    @PrePersist
    private void onCreate(){
        creationDate = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TransferEntity that = (TransferEntity) o;
        return operationTransferId != null && Objects.equals(operationTransferId, that.operationTransferId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
