package com.maraujo.corebanking.core.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TransferRequest {
    private UUID operationId;
    private Integer accountFrom;
    private Integer accountTo;
    private Long amount;

}
