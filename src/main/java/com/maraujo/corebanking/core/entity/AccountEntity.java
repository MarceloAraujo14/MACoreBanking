package com.maraujo.corebanking.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "account")
public class AccountEntity {
    @Id
    Integer accountNumber;
    Long balance;
    boolean lock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccountEntity that = (AccountEntity) o;
        return accountNumber != null && Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
