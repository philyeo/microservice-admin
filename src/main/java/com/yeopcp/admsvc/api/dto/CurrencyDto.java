package com.yeopcp.admsvc.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

public class CurrencyDto {

    @Getter
    final public UUID id;

    @Getter
    @Setter
    final public Date createdAt;

    @Getter
    @Setter
    final public Date updatedAt;

    @Getter
    @Setter
    final public String key;

    @Getter
    @Setter
    final public String currencyLabel;

    public CurrencyDto(UUID id, Date createdAt, Date updatedAt, String key, String currencyLabel) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.key = key;
        this.currencyLabel = currencyLabel;
    }

}
